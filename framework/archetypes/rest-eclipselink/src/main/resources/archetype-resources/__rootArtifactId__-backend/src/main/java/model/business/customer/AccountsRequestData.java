#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ${package}.model.business.customer;


import org.appverse.web.framework.backend.api.model.business.AbstractBusinessBean;

public class AccountsRequestData extends AbstractBusinessBean{

    private FamilyName family;

    public AccountsRequestData() {
    }

    /**
     * @return the familyNameVO
     */
    public FamilyName getFamilyName() {
        return family;
    }

    /**
     * @param family the familyNameVO to set
     */
    public void setFamilyName(FamilyName family) {
        this.family = family;
    }
    
    
}
