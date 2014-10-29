#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.customer;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
#end
import ${package}.model.presentation.common.ResultDataVO;

/**
 *
 * @author maps
 */
#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value="AccountsDataVO Model")
#end
public class AccountsDataVO extends ResultDataVO {
    
    /**
     * Accounts Array returned as a result for the service query.
     */
    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value="result")
    #end
    private AccountVO[] result;
    
    //private Account[] beneficiaryFamilies;
    #if ( !$null.isNull($swagger) && $swagger == 'true' )
    @ApiModelProperty(value="families")
    #end
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
