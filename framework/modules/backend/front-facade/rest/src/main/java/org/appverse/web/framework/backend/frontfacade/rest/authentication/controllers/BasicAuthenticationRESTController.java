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
package org.appverse.web.framework.backend.frontfacade.rest.authentication.controllers;

import org.appverse.web.framework.backend.api.helpers.security.SecurityHelper;
import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.services.presentation.AuthenticationServiceFacade;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.inject.Singleton;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Singleton
@Path("/sec")
/**
 * This JAX-RS (Jersey) controller exposes an Basic Autentication service for REST services providing a "login" service.
 * The controller obtains "Authorization" header, accordding to Basic Authentication, from the request in order to obtain
 * username and password and delegates in a service that authenticates the user.
 * The controller creates an HttpSession so that the user is already authenticated in successive requests and adds a
 * "JSESSIONID" cookie to the response.
 *
 * Remember to register your controller using Jersey2 application in your project and JacksonFeature
 * Example:
 * register(JacksonFeature.class);
 * register(BasicAuthenticationRESTController.class);
 *
 */
public class BasicAuthenticationRESTController implements ApplicationContextAware {

    private String AUTHENTICATION_SERVICE_NAME = "authenticationServiceFacade";

    /**
     * With Jersey 2.x applicationContext is not Autowired (see https://java.net/jira/browse/JERSEY-2169)
     * It must implement ApplicationContextAware to get the ApplicationContext instance
     */

    private ApplicationContext applicationContext;

    private AuthenticationServiceFacade authenticationServiceFacade;

    /*
        Automatic JSON conversion
        See http://stackoverflow.com/questions/21039620/upgrading-from-jersey-1-to-jersey-2-5
    */


    /**
     * Authenticates an user. Requires basic authentication header.
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws Exception
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(@Context HttpServletRequest httpServletRequest,
                                     @Context HttpServletResponse httpServletResponse) throws Exception {

        String[] userNameAndPassword;

        // Invalidate session if exists
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) httpSession.invalidate();

        authenticationServiceFacade = (AuthenticationServiceFacade) applicationContext.getBean(AUTHENTICATION_SERVICE_NAME);

        try{
            userNameAndPassword = obtainUserAndPasswordFromBasicAuthenticationHeader(httpServletRequest);
        }
        catch (BadCredentialsException e){
            httpServletResponse.addHeader("WWW-Authenticate", "Basic");
            return Response.status(Response.Status.UNAUTHORIZED).entity(new AuthorizationDataVO()).build();
        }

        //Create and set the cookie
        httpServletRequest.getSession(true);
        String jsessionId = httpServletRequest.getSession().getId();
        Cookie sessionIdCookie = new Cookie("JSESSIONID", jsessionId);
        httpServletResponse.addCookie(sessionIdCookie);

        // Obtain XSRFToken and add it as a response header
        String xsrfToken = SecurityHelper.createXSRFToken(httpServletRequest);
        httpServletResponse.addHeader(SecurityHelper.XSRF_TOKEN_NAME, xsrfToken);

        // Authenticate principal and return authorization data
        AuthorizationDataVO authData = authenticationServiceFacade.authenticatePrincipal(userNameAndPassword[0], userNameAndPassword[1]);

        // AuthorizationDataVO
        return Response.status(Response.Status.OK).entity(authData).build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    private String[] obtainUserAndPasswordFromBasicAuthenticationHeader(HttpServletRequest httpServletRequest) throws Exception{
        // Authorization header
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader == null) {
            throw new BadCredentialsException("Authorization header not found");
        }

        // Decode the authorization string
        BASE64Decoder decoder = new BASE64Decoder();
        String token;
        try{
            byte[] decoded = decoder.decodeBuffer(authHeader.substring(6));
            token = new String(decoded);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        int separator = token.indexOf(":");

        if (separator == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] {token.substring(0, separator), token.substring(separator + 1)};
    }

}
