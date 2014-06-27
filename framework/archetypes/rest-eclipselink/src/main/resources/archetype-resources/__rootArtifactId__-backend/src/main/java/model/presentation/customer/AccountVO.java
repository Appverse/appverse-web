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
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;

/**
 *
 * @author maps
 */
@ApiModel(value="AccountVO Model")
public class AccountVO extends AbstractPresentationBean{

    /**
     * AccountVO number (unique identifier)
     */
    @ApiModelProperty(value = "number")
    private String number;
    
    /**
     * Another account identifier used in some countries
     */
    @ApiModelProperty(value = "id")
    private String id;
    
    /**
     * AccountVO alias (name)
     */
    @ApiModelProperty(value = "alias")
    private String alias;
    
    /**
     * Current AccountVO balance
     */
    @ApiModelProperty(value = "balance")
    private double balance;
    
    /**
     * Current AccountVO balance currency
     */
    @ApiModelProperty(value = "balanceCurrency")
    private String balanceCurrency;
    
    /**
     * The account type (mainly used for accounts grouping)
     */
    @ApiModelProperty(value = "familyCode")
    private String familyCode;
    
    /**
     * AccountVO owner
     */
    @ApiModelProperty(value = "owner")
    private String owner;

    @ApiModelProperty(value = "lastMothBalance")
    private double lastMothBalance;

    @ApiModelProperty(value = "lastMonthBalanceCurrency")
    private String lastMonthBalanceCurrency;
    
    /**
     * Constructor
     */
    public AccountVO() {
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * @return the balanceCurrency
     */
    public String getBalanceCurrency() {
        return balanceCurrency;
    }

    /**
     * @param balanceCurrency the balanceCurrency to set
     */
    public void setBalanceCurrency(String balanceCurrency) {
        this.balanceCurrency = balanceCurrency;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the familyCode
     */
    public String getFamilyCode() {
        return familyCode;
    }

    /**
     * @param familyCode the familyCode to set
     */
    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    /**
     * @return the LastMothBalance
     */
    public double getLastMothBalance() {
        return lastMothBalance;
    }

    /**
     * @param LastMothBalance the LastMothBalance to set
     */
    public void setLastMothBalance(double LastMothBalance) {
        this.lastMothBalance = LastMothBalance;
    }

    /**
     * @return the LastMonthBalanceCurrency
     */
    public String getLastMonthBalanceCurrency() {
        return lastMonthBalanceCurrency;
    }

    /**
     * @param LastMonthBalanceCurrency the LastMonthBalanceCurrency to set
     */
    public void setLastMonthBalanceCurrency(String LastMonthBalanceCurrency) {
        this.lastMonthBalanceCurrency = LastMonthBalanceCurrency;
    }
    
    
}
