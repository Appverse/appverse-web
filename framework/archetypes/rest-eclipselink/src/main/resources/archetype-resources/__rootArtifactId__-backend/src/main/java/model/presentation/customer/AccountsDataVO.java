#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ${package}.model.presentation.customer;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import ${package}.model.presentation.common.ResultDataVO;

/**
 *
 * @author maps
 */
@ApiModel(value="AccountsDataVO Model")
public class AccountsDataVO extends ResultDataVO {
    
    /**
     * Accounts Array returned as a result for the service query.
     */
    @ApiModelProperty(value="result")
    private AccountVO[] result;
    
    //private Account[] beneficiaryFamilies;
    @ApiModelProperty(value="families")
    private FamilyVO[] families;

    public FamilyVO[] getFamilies() {
        return families;
    }

    public void setFamilies(FamilyVO[] families) {
        this.families = families;
    }

    /**
     * Constructor
     */
    public AccountsDataVO() {
    }

    /**
     * @return the result
     */
    public AccountVO[] getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(AccountVO[] result) {
        this.result = result;
    }
    
    
    
}
