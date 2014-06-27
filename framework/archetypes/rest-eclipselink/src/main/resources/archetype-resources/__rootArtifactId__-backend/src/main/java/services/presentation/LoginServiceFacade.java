#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services.presentation;

import ${package}.model.presentation.common.LoginRequestDataVO;
import ${package}.model.presentation.common.ResultDataVO;
import ${package}.model.presentation.common.UserDataVO;
import org.appverse.web.framework.backend.api.services.presentation.IPresentationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by MCRZ on 28/02/14.
 */

public interface LoginServiceFacade extends IPresentationService{

    UserDataVO login(LoginRequestDataVO requestData) throws Exception;
    ResultDataVO logout(HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
