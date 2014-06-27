#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;

/**
 * @author maps
 * @version 1.0
 * @created 08-may-2012 9:45:23
 */
@ApiModel(value = "ResultDataVO model")
public class ResultDataVO extends AbstractPresentationBean{



    /**
     * The error returned for this Result Data. Code is 0 if result is returned with success
     */
    @ApiModelProperty(value = "error")
    private ErrorVO error;

    /**
     * Constructor
     */
    public ResultDataVO(){

    }

    /**
     * @return the error
     */
    public ErrorVO getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(ErrorVO error) {
        this.error = error;
    }


}
