/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License 
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this 
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
package org.appverse.web.framework.backend.frontfacade.gxt.controllers;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.appverse.web.framework.backend.api.services.presentation.AuthenticationServiceFacade;
import org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTPresentationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Controller;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Controller
@Path("/")
public class GwtRpcController extends RemoteServiceServlet {

	private static final long serialVersionUID = 4147354200774086984L;

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private ServletContext servletContext;
	private final ThreadLocal<String> serviceName = new ThreadLocal<String>();

	private void checkXSRFToken(final HttpServletRequest request)
			throws IOException {
		String requestValue = request.getHeader("X-XSRF-Cookie");
		String sessionValue = (String) request.getSession().getAttribute(
				"X-XSRF-Cookie");
		if (sessionValue != null && !sessionValue.equals(requestValue)) {
			throw new PreAuthenticatedCredentialsNotFoundException(
					"XSRF attribute not found in session.");

		}
	}

	private String createXSRFToken(final HttpServletRequest request)
			throws IOException {
		HttpSession session = request.getSession();
		String xsrfSessionToken = (String) session
				.getAttribute("X-XSRF-Cookie");
		if (xsrfSessionToken == null) {
			Random r = new Random(System.currentTimeMillis());
			long value = System.currentTimeMillis() + r.nextLong();
			char ids[] = session.getId().toCharArray();
			for (int i = 0; i < ids.length; i++) {
				value += ids[i] * (i + 1);
			}
			xsrfSessionToken = Long.toString(value);
			session.setAttribute("X-XSRF-Cookie", xsrfSessionToken);
		}
		return xsrfSessionToken;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@POST
	@Path("{servicemethodname}")
	public String handleRequest(
			@PathParam("servicemethodname") String servicemethodname,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {
		
//		String path = request.getServletPath();
		serviceName.set(servicemethodname.substring(0,
				servicemethodname.lastIndexOf('.')));
		Object presentationService = applicationContext.getBean(serviceName
				.get());
		if (!(presentationService instanceof AuthenticationServiceFacade)) {
			checkXSRFToken(request);
		}
		super.doPost(request, response);
		return "";
	}

	@Override
	public String processCall(final String payload)
			throws SerializationException {
		try {
			Object presentationService = applicationContext.getBean(serviceName
					.get());
			if (!(presentationService instanceof RemoteService)) {
				throw new IllegalArgumentException(
						"Requested Spring Bean is not a GWT RemoteService Presentation Service: "
								+ payload + " (" + presentationService + ")");
			}

			RPCRequest rpcRequest = RPC.decodeRequest(payload,
					presentationService.getClass(), this);
			if (presentationService instanceof AuthenticationServiceFacade
					&& rpcRequest.getMethod().equals(
							AuthenticationServiceFacade.class
									.getMethod("getXSRFSessionToken"))) {
				return RPC.encodeResponseForSuccess(rpcRequest.getMethod(),
						createXSRFToken(getThreadLocalRequest()));
			}
			return RPC.invokeAndEncodeResponse(presentationService,
					rpcRequest.getMethod(), rpcRequest.getParameters(),
					rpcRequest.getSerializationPolicy(), rpcRequest.getFlags());
		} catch (Exception e) {
			GWTPresentationException pex = new GWTPresentationException(
					e.getMessage());
			return RPC.encodeResponseForFailure(null, pex);
		}
	}
}