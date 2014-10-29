#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
#end


#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value = "ErrorVO model")
#end
public class ErrorVO {

    /**
     * Error Code for error handling. 0 for success.
     */
    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value="code")
    #end
    private long code;

    /**
     * Error Message to be displayed
     */
    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value="message")
    #end
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