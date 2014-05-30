/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License 
 Version 2.0 (â€œAPL v2.0â€�). If a copy of the APL was not distributed with this 
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the AppVerse Public License v2.0 
 are met.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) 
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.
 */
package org.appverse.web.framework.backend.frontfacade.json.controllers;

import org.appverse.web.framework.backend.api.helpers.json.JacksonContextResolver;
import org.appverse.web.framework.backend.api.helpers.security.SecurityHelper;
import org.appverse.web.framework.backend.api.services.presentation.AuthenticationServiceFacade;
import org.appverse.web.framework.backend.frontfacade.json.controllers.exceptions.BadRequestException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Component
@Singleton
@Path("jsonservices")
public class JSONController implements ApplicationContextAware {
    /**
     * With Jersey 2.x applicationContext is not Autowired (see https://java.net/jira/browse/JERSEY-2169)
     * It must implement ApplicationContextAware to get the ApplicationContext instance
     */
    private ApplicationContext applicationContext;

	@Autowired
	CustomMappingJacksonHttpMessageConverter customMappingJacksonHttpMessageConverter;

	private void addDefaultResponseHeaders(HttpServletResponse response) {
		// Add headers to prevent Cross-site ajax calls issues
		response.addHeader("Content-Type", "application/json; charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers",
				"Content-Type,X-Requested-With");
	}

	@PostConstruct
	public void bindMessageConverters() {
		ObjectMapper mapper = new ObjectMapper();
		// mapper.setDateFormat(new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"));
		// SerializationConfig sc = mapper.getSerializationConfig();
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.registerModule(JacksonContextResolver.getCustomSimpleModuleWithXSS());

		customMappingJacksonHttpMessageConverter.setObjectMapper(mapper);
	}

	// @POST
	// @Consumes("application/json")
	// @Produces("application/json")
	// @Path("*.json")
	// public String handleRequest1(@Context HttpServletRequest request,
	// @Context HttpServletResponse response, @FormParam("payload") String
	// payload)
	// throws Exception {
	// System.out.println("handle request 1");
	// return "";
	// }

	/**
	 * Method to handle all requests to the Appverse Services Presentation
	 * Layer. It only accepts POST requests, with the parameter set on the
	 * payload. The URL must contain the servicename (spring name of the
	 * Presentation Service) and also the method name. The URL musb be something
	 * like: {protocol}://{host}:{port}/{appcontext}/{servicename}/{methodname}
	 * 
	 * @param requestServiceName
	 *            The "spring" name of the Service.
	 * @param requestMethodName
	 *            The method name
	 * @param response
	 *            The HttpServletResponse, injected by Jersey.
	 * @param payload
	 *            The payload must contain the parameter as json.
	 * @return
	 * @throws Exception
	 *             In case of any Bad Request or an uncontrolled exception
	 *             raised by the Service.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Path("{servicename}/{methodname}")
	public String handleRequest(
			@PathParam("servicename") String requestServiceName,
			@PathParam("methodname") String requestMethodName,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, String payload)
			throws Exception {
		// String path = request.getServletPath();
		System.out.println("Request Received - " + requestServiceName + "."
				+ requestMethodName);

		Object presentationService = applicationContext
				.getBean(requestServiceName);
		if (presentationService == null) {
			throw new BadRequestException(
					"Requested ServiceFacade don't exists "
							+ requestServiceName);
		}
		if (!(presentationService instanceof AuthenticationServiceFacade)) {
		    SecurityHelper.checkXSRFToken(request);
        }
        if (presentationService instanceof AuthenticationServiceFacade
                && requestMethodName.equals("getXSRFSessionToken")) {
            ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(
                    response);
            customMappingJacksonHttpMessageConverter.write(
                        SecurityHelper.createXSRFToken(request),
                        org.springframework.http.MediaType.APPLICATION_JSON,
                        outputMessage);
            addDefaultResponseHeaders(response);
            return "";

        }

        // Determine if method exist by name.
		Method[] methods = presentationService.getClass().getMethods();
		List<Method> availableMethod = new ArrayList<Method>();
		for (Method methodItem : methods) {
			if (methodItem.getName().equals(requestMethodName)) {
				availableMethod.add(methodItem);
				// method = methodItem;
				// break;
			}
		}
		if (availableMethod != null && availableMethod.size() == 0) {
			throw new BadRequestException("Requested Method don't exists "
					+ requestMethodName + " for serviceFacade "
					+ requestServiceName);
		}
		boolean badRequest = false;
		StringBuffer sbf = new StringBuffer();
		Method methodFound = null;
		Object parameterFound = null;
		Object[] parametersFound = null;
		// Identify on the available methods the correct method to execute,
		// based on the parameters and its types (trying to convert them).
		for (Method methodItem : availableMethod) {
			Class<?>[] parameterTypes = methodItem.getParameterTypes();
			Class<?> parameterType = null;
			if (parameterTypes.length > 1) {
				try {
					parametersFound = customMappingJacksonHttpMessageConverter.readInternal(parameterTypes, payload);
					methodFound = methodItem;
					badRequest = false;
					//method found and objects correctly parsed
					break;
				}catch(Throwable th) {
					badRequest = true;
					sbf.append("{Error parsing json ["+th.getMessage()+"]");
				}
			}
			if (parameterTypes.length > 0) {
				parameterType = parameterTypes[0];
				try {
					parameterFound = customMappingJacksonHttpMessageConverter
							.readInternal(parameterType, payload);
					methodFound = methodItem;
					badRequest = false;
					break; //found the correct method to execute.
				} catch (Throwable th) {
					badRequest = true;
					sbf.append("{Parameter of type "
							+ parameterType.getCanonicalName()
							+ " can't be parsed}");
				}
			} else {
				// only accepting parameters less methods in case payload is
				// empty
				if (payload != null && payload.length() == 0) {
					methodFound = methodItem;
					badRequest = false;
					break; //found the correct method to execute
				}
			}
		}
		if (badRequest) {
			sbf.append(" from [" + payload + "]");
			throw new BadRequestException(sbf.toString());
		}
		try {
			//invoke the method
			Object result = null;
			if (parameterFound != null) { //method with one parameter
				result = methodFound
						.invoke(presentationService, parameterFound);
			} else if (parametersFound != null ){ //method with multiple parameters
				result = methodFound.invoke(presentationService, parametersFound);
			} else { //method with no parameters
				result = methodFound.invoke(presentationService);
			}
			// return Response.ok(result, MediaType.APPLICATION_JSON).build();
			ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(
					response);
           if( result != null ) {
               //only write result in case it is not void!
                customMappingJacksonHttpMessageConverter.write(result,
                        org.springframework.http.MediaType.APPLICATION_JSON,
                        outputMessage);
               }
			addDefaultResponseHeaders(response);
		} catch (Throwable th) {
			// response.sendError(500, th.getMessage());
			th.printStackTrace();
            throw new WebApplicationException(
                    Response.status(
                            Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Service Internal Error [" + th.getCause() != null ? th
                                    .getCause().getMessage() : th.getMessage() + "]")
                            .build()
            );
			/*ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.entity("Service Internal Error [" + th.getCause() != null ? th
					.getCause().getMessage() : th.getMessage() + "]");
			Response resp = builder.build();
			throw new WebApplicationException(resp);*/
		}
		return "";

	}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}