package org.appverse.web.framework.backend.frontfacade.rest.authentication.controllers;

import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.services.presentation.AuthenticationServiceFacade;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
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
import java.io.IOException;
import java.util.Random;

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

        // Invalidate session if exists
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) httpSession.invalidate();

        authenticationServiceFacade = (AuthenticationServiceFacade) applicationContext.getBean(AUTHENTICATION_SERVICE_NAME);

        String userName = null;
        String password = null;

        try{
            // Authorization header
            String authHeader = httpServletRequest.getHeader("Authorization");

            // Decode the authorization string
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decoded = decoder.decodeBuffer(authHeader.substring(6));
            String userNameAndPass = new String(decoded);

            // Get user name and password
            userName=userNameAndPass.substring(0,userNameAndPass.indexOf(":"));
            password=userNameAndPass.substring(userNameAndPass.indexOf(":")+1);
        }
        catch (Exception e){

            // httpServletResponse.setStatus(Response.Status.FORBIDDEN.getStatusCode());
            // httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.addHeader("WWW-Authenticate", "Basic");
            return Response.status(Response.Status.UNAUTHORIZED).entity(new AuthorizationDataVO()).build();
            // return Response.status(401).entity(new AuthorizationDataVO()).build();
        }

        //Create and set the cookie
        httpServletRequest.getSession(true);
        String jsessionId = httpServletRequest.getSession().getId();
        Cookie sessionIdCookie = new Cookie("JSESSIONID", jsessionId);
        httpServletResponse.addCookie(sessionIdCookie);

        // Authenticate principal and return authorization data
        AuthorizationDataVO authData = authenticationServiceFacade.authenticatePrincipal(userName, password);

        // Set the session attribute that will indicate the user is authenticated
        WebUtils.setSessionAttribute(httpServletRequest, "REMOTE_USER", userName);

        // AuthorizationDataVO
        return Response.status(Response.Status.OK).entity(authData).build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // TODO
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

    // TODO
    private void checkXSRFToken(final HttpServletRequest request)
            throws Exception {
        /**
         * Currently this method is not used. Needs to be analyzed how this can
         * be implemented in a "generic" way.
         */
        String requestValue = request.getHeader("X-XSRF-Cookie");
        String sessionValue = (String) request.getSession().getAttribute(
                "X-XSRF-Cookie");
        if (sessionValue != null && !sessionValue.equals(requestValue)) {
            // throw new PreAuthenticatedCredentialsNotFoundException(
            // "XSRF attribute not found in session.");
            throw new Exception("XSRF attribute not found in session.");
        }
    }
}
