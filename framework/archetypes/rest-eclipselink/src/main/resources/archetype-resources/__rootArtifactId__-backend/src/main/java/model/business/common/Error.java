#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.business.common;

import org.appverse.web.framework.backend.api.model.business.AbstractBusinessBean;

/**
 * @author maps
 * @version 1.0
 * @created 08-may-2012 9:45:23
 */
public class Error extends AbstractBusinessBean {

    /**
     * Error Code for error handling. 0 for success.
     */
    private long code;

    /**
     * Error Message to be displayed
     */
    private String message;

    /**
     * Constructor
     */
    public Error(){

    }

    /**
     * @return the code
     */
    public long getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(long code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }



}