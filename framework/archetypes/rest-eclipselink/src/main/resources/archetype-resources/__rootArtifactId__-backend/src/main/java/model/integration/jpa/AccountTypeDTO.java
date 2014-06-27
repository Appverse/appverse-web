#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.integration.jpa;

import org.appverse.web.framework.backend.persistence.model.integration.AbstractIntegrationJPABean;

import javax.persistence.*;

/**
 * Created by MCRZ on 28/02/14.
 */
@Entity
@Table(name = "ACCOUNT_TYPE")
public class AccountTypeDTO extends AbstractIntegrationJPABean{

    private String code;
    private String description;

    @Id
    @TableGenerator(name = "ACCOUNT_TYPE_GEN", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "ACCOUNT_TYPE_SEQ", valueColumnName = "SEQ_COUNT", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ACCOUNT_TYPE_GEN")
    public long getId() {
        return id;
    }

    @Column(nullable = true, length = 40)
    public String getCode() {
        return code;
    }

    @Column(nullable = true, length = 128)
    public String getDescription() {
        return description;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
