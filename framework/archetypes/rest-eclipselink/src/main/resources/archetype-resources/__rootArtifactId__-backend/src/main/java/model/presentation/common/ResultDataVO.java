#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.common;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
#end
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value = "ResultDataVO model")
#end
public class ResultDataVO extends AbstractPresentationBean{



    /**
     * The error returned for this Result Data. Code is 0 if result is returned with success
     */
    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "error")
    #end
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
