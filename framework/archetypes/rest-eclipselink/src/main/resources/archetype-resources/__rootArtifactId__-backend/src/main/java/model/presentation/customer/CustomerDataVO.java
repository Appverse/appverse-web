#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.customer;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
#end
import ${package}.model.presentation.common.ResultDataVO;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value = "UserDataVO model")
#end
public class CustomerDataVO extends ResultDataVO {

    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value = "result")
    #end
    private CustomerInfoVO result;

    public CustomerInfoVO getResult() {
        return result;
    }

    public void setResult(CustomerInfoVO result) {
        this.result = result;
    }
}

