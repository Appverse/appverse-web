#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.presentation.customer;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
import com.wordnik.swagger.annotations.ApiModel;
#end
import ${package}.model.presentation.common.ResultDataVO;

#if ( !$null.isNull($swagger) && $swagger == 'true' )
@ApiModel(value="GlobalPositonDataVO Model")
#end
public class GlobalPositionDataVO extends ResultDataVO {

    /**
     * Accounts Array returned as a result for the service query.
     */
    private AccountVO[] result;
    
    /**
     * Grouped accounts families (containing the aggregated balance for each family)
     */
    private FamilyVO[] families;
    
    /**
     * Constructor
     */
    public GlobalPositionDataVO() {
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

    /**
     * @return the families
     */
    public FamilyVO[] getFamilies() {
        return families;
    }

    /**
     * @param families the families to set
     */
    public void setFamilies(FamilyVO[] families) {
        this.families = families;
    }
    
}
