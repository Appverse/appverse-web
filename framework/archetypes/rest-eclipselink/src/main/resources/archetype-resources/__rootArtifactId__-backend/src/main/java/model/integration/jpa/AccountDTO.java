#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.integration.jpa;

import org.appverse.web.framework.backend.persistence.model.integration.AbstractIntegrationAuditedJPABean;

import javax.persistence.*;

/**
 * Created by MCRZ on 28/02/14.
 */
@Entity
@Table(name = "ACCOUNT")
public class AccountDTO extends AbstractIntegrationAuditedJPABean {

    /**
     * AccountVO number (unique identifier)
     */
    private String number;

    /**
     * AccountVO owner
     */
    private AccountUserDTO accountUser;

    /**
     * Current AccountVO balance
     */
    private double balance;

    /**
     * Current AccountVO balance currency
     */
    private String balanceCurrency;

    /**
     * The account type (mainly used for accounts grouping)
     */
    private AccountTypeDTO accountType;

    private double lastMonthBalance;

    private String lastMonthBalanceCurrency;

    @Id
    @TableGenerator(name = "ACCOUNT_GEN", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "ACCOUNT_SEQ", valueColumnName = "SEQ_COUNT", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ACCOUNT_GEN")
    public long getId() {
        return id;
    }

    @Column(name="NUMBER",nullable = false, length = 40, unique = true)
    public String getNumber() {
        return number;
    }

    @OneToOne(orphanRemoval = true, optional = true, mappedBy="account")
    public AccountUserDTO getAccountUser() {
        return accountUser;
    }

    @Column(nullable = false)
    public double getBalance() {
        return balance;
    }

    @Column(name = "BALANCE_CURRENCY",nullable = false,length = 3)
    public String getBalanceCurrency() {
        return balanceCurrency;
    }

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_TYPE", nullable=false)
    public AccountTypeDTO getAccountType() {
        return accountType;
    }

    @Column(name = "LASTMONTH_BALANCE", nullable = false)
    public double getLastMonthBalance() {
        return lastMonthBalance;
    }

    @Column(name = "LASTMONTH_BALANCE_CURRENCY", nullable = false, length = 3)
    public String getLastMonthBalanceCurrency() {
        return lastMonthBalanceCurrency;
    }


    @Override
    @Version
    public long getVersion() {
        return version;
    }

    public void setNumber(String accountNumber) {
        this.number = accountNumber;
    }

    public void setAccountUser(AccountUserDTO accountUser) {
        this.accountUser = accountUser;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setBalanceCurrency(String balanceCurrency) {
        this.balanceCurrency = balanceCurrency;
    }

    public void setAccountType(AccountTypeDTO accountType) {
        this.accountType = accountType;
    }

    public void setLastMonthBalance(double lastMonthBalance) {
        this.lastMonthBalance = lastMonthBalance;
    }

    public void setLastMonthBalanceCurrency(String lastMonthBalanceCurrency) {
        this.lastMonthBalanceCurrency = lastMonthBalanceCurrency;
    }

}
