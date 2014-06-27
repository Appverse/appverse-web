#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author maps
 * @version 1.0
 * @created 08-may-2012 9:45:23
 */
@ApiModel(value = "ErrorVO model")
public class ErrorVO {

    /**
     * Error Code for error handling. 0 for success.
     */
    @ApiModelProperty(value="code")
    private long code;

    /**
     * Error Message to be displayed
     */
    @ApiModelProperty(value="message")
    private String message;

    /**
     * Constructor
     */
    public ErrorVO(){

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