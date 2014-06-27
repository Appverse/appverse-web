#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.integration.jpa;

import org.appverse.web.framework.backend.persistence.model.integration.AbstractIntegrationAuditedJPABean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by MCRZ on 28/02/14.
 */
@Entity
@Table(name = "ACCOUNT_USER")
public class AccountUserDTO extends AbstractIntegrationAuditedJPABean {

    private String alias;

    private UserDTO owner;

    private AccountDTO account;

    private Date date;


    @Id
    @TableGenerator(name = "ACCOUNT_USER_GEN", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "ACCOUNT_USER_SEQ", valueColumnName = "SEQ_COUNT", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ACCOUNT_USER_GEN")
    public long getId() {
        return id;
    }

    @Column(nullable = true, length = 40)
    public String getAlias() {
        return alias;
    }

    @OneToOne
    @JoinColumn(name="ACCOUNT_ID", nullable=false, updatable=false)
    public AccountDTO getAccount() {
        return account;
    }

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable=false)
    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO user) {
        this.owner = user;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
