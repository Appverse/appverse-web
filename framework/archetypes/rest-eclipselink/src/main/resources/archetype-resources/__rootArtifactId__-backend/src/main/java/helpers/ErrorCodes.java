#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.helpers;

import ${package}.model.business.common.Error;
import ${package}.model.presentation.common.ErrorVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by MCRZ on 3/03/14.
 */
public class ErrorCodes {

    private Logger log = LoggerFactory.getLogger(ErrorCodes.class);


    public static long SUCCESS_CODE                              = 0;
    public static long ERROR_CODE_unhandled                      = 99;
    public static long ERROR_CODE_noresultsfound                 = 1;
    public static long ERROR_CODE_jsonerror                      = 2;
    public static long ERROR_CODE_badrequest_missingdata         = 3;
    private static long ERROR_CODE_UNHANDLED_EXCEPTION           = 99;

    //login
    public static long ERROR_CODE_prelogin_expired               = 4;
    public static long ERROR_CODE_login_wrongcredentials         = 90;
    public static long ERROR_CODE_login_alreadylogged            = 91;
    public static long ERROR_CODE_logout_notlogged               = 92;
    public static long ERROR_CODE_session_expired                = 93;
    public static long ERROR_CODE_session_required               = 94;

    private Properties errorCodes;

    public void setErrorCodes(Properties errorCodes){
        this.errorCodes = errorCodes;
    }

    public Properties getErrorCodes(){
        return errorCodes;
    }
    public String getDescriptionError(long key){
        String message = errorCodes.getProperty(String.valueOf(key));
        if (message == null){
            log.warn("Error code {key}not found", key);
            message = "No errorCode found"+key;
        }
        return message;
    }
    public ErrorVO getErrorVO(long errorCode){
        ErrorVO error = new ErrorVO();
        error.setCode(errorCode);
        error.setMessage(getDescriptionError(errorCode));
        return error;
    }
    public Error getError(long errorCode){
        Error error = new Error();
        error.setCode(errorCode);
        error.setMessage(getDescriptionError(errorCode));
        return error;
    }


}
