#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ${package}.model.business.customer;

import ${package}.model.business.common.ResultData;


/**
 *
 * @author maps
 */
public class AccountsData extends ResultData {

    /**
     * Accounts Array returned as a result for the service query.
     */
    private Account[] result;

    /**
     * Grouped accounts families (containing the aggregated balance for each family)
     */
    private Family[] families;

    /**
     * Constructor
     */
    public AccountsData() {
    }

    /**
     * @return the result
     */
    public Account[] getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Account[] result) {
        this.result = result;
    }

    /**
     * @return the families
     */
    public Family[] getFamilies() {
        return families;
    }

    /**
     * @param families the families to set
     */
    public void setFamilies(Family[] families) {
        this.families = families;
    }
    
}
