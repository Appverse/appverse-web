#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.customer;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import ${package}.model.presentation.common.ResultDataVO;

/**
 * Created by MCRZ on 16/04/2014.
 */
@ApiModel(value = "UserDataVO model")
public class CustomerDataVO extends ResultDataVO {

    @ApiModelProperty(value = "result")
    private CustomerInfoVO result;

    public CustomerInfoVO getResult() {
        return result;
    }

    public void setResult(CustomerInfoVO result) {
        this.result = result;
    }
}

