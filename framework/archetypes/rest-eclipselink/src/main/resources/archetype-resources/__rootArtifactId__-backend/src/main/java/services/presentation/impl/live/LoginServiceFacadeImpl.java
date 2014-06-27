#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services.presentation.impl.live;


import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import ${package}.converters.p2b.UserP2BBeanConverter;
import ${package}.helpers.ErrorCodes;
import ${package}.model.business.customer.User;
import ${package}.model.presentation.common.LoginRequestDataVO;
import ${package}.model.presentation.common.ResultDataVO;
import ${package}.model.presentation.common.UserDataVO;
import ${package}.model.presentation.common.UserVO;
import ${package}.services.business.LoginService;
import ${package}.services.presentation.LoginServiceFacade;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.services.presentation.AbstractPresentationService;
import org.appverse.web.framework.backend.api.services.presentation.PresentationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by MCRZ on 3/03/14.
 */
@Controller
@Path("/login")
@Api(value = "/login", description = "Login operations")
public class LoginServiceFacadeImpl extends AbstractPresentationService implements LoginServiceFacade {


    @AutowiredLogger
    private static Logger logger;

    @Autowired
    protected ErrorCodes errorCodes;

    @Autowired
    protected LoginService loginService;
    @Autowired
    protected UserP2BBeanConverter userP2BBeanConverter;

    //login
    @Path("/login")
    @ApiOperation(value = "login", notes = "The login method", response = UserDataVO.class)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDataVO login(
            LoginRequestDataVO requestData) throws Exception{
        UserDataVO userDataVO = new UserDataVO();
        if(requestData == null || StringUtils.isEmpty(requestData.getUsername()) || StringUtils.isEmpty(requestData.getPassword())){
            throw new PresentationException(ErrorCodes.ERROR_CODE_badrequest_missingdata);
        }
        String username = requestData.getUsername();
        String password = requestData.getPassword();
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        //tries to authenticate if user does not exist then AuthenticationException will be raised
        User user = loginService.authenticate(authenticationToken);

        if (user != null) {
            //generates user response
            userDataVO.setResult(generateUserVO(user));
            userDataVO.setError(errorCodes.getErrorVO(ErrorCodes.SUCCESS_CODE));
        } else {
            throw new PresentationException(ErrorCodes.ERROR_CODE_login_wrongcredentials);
        }

        return userDataVO;
    }

    //secure/logout
    @Path("/logout")
    @POST
    @ApiOperation(value = "logout", notes = "Logout", response = ResultDataVO.class)
    public ResultDataVO logout(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws Exception{
        logger.info("logout");
        loginService.logout();
        SecurityContextHolder.getContext().setAuthentication(null);
        ResultDataVO userDataVO = new ResultDataVO();
        userDataVO.setError(errorCodes.getErrorVO(ErrorCodes.SUCCESS_CODE));
        req.getSession().invalidate();
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (int i = 0; i < cookies.length; i++) {
                cookies[i].setValue("");
                cookies[i].setMaxAge(0);
                resp.addCookie(cookies[i]);
            }
        return userDataVO;
    }

    /**
     * Convert user to userVO
     * @param user
     * @return
     * @throws Exception
     */
    private UserVO generateUserVO(User user)throws Exception{
        UserVO userVO = userP2BBeanConverter.convert(user);
        return userVO;
    }

}
