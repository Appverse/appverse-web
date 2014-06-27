#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.integration.jpa;

import org.appverse.web.framework.backend.persistence.model.integration.AbstractIntegrationAuditedJPABean;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER")
public class UserDTO extends AbstractIntegrationAuditedJPABean {

	private static final long serialVersionUID = 1L;

    private String title;
	private String name;
	private String lastName;
    private String username;
	private String email;
	private String password;
    private String reference;
    private String country;
    private List<AccountUserDTO> accounts;

	private boolean active = true;
    private Date lastLoggedDate;

    private String idCard;
    private String address;
    private String mobileNumber;
    private String phoneNumber;


	public UserDTO() {
	}

	@Column(nullable = false, unique = true, length = 40)
	public String getEmail() {
		return email;
	}

	@Id
	@TableGenerator(name = "USER_GEN", table = "SEQUENCE", pkColumnName = "SEQ_NAME", pkColumnValue = "USER_SEQ", valueColumnName = "SEQ_COUNT", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "USER_GEN")
	public long getId() {
		return id;
	}

    @Column(nullable = true, length = 4)
    public String getTitle() {
        return title;
    }

	@Column(nullable = false, length = 40)
	public String getLastName() {
		return lastName;
	}

    @Column(nullable = false, length = 40, unique = true)
    public String getUsername() {
        return username;
    }

    @Column(nullable = false, length = 2)
    public String getCountry(){
        return country;
    }

    @Column(nullable = false, length = 40)
	public String getName() {
		return name;
	}

	@Column(nullable = false, length = 40)
	public String getPassword() {
		return password;
	}

    @Column(nullable = false, length = 40)
    public String getReference() {
        return reference;
    }

    @Column(name = "LAST_LOGGED_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastLoggedDate() {
        return lastLoggedDate;
    }

	@Override
	@Version
	public long getVersion() {
		return version;
	}

	@Column(nullable = false)
	public boolean isActive() {
		return active;
	}

    @OneToMany(mappedBy="owner")
    public List<AccountUserDTO> getAccounts() {
        return accounts;
    }

    @Column(name="ID_CARD", nullable = false)
    public String getIdCard() {
        return idCard;
    }

    @Column(nullable = false)
    public String getAddress() {
        return address;
    }

    @Column(name="MOBILE_NUMBER", nullable = false)
    public String getMobileNumber() {
        return mobileNumber;
    }

    @Column(name="PHONE_NUMBER", nullable = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setAccounts(List<AccountUserDTO> accounts) {
        this.accounts = accounts;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
	public void setActive(final boolean active) {
		this.active = active;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
    public void setUsername(final String username) { this.username = username; }

	public void setName(final String name) {
		this.name = name;
	}

    public void setLastLoggedDate(Date lastLoggedDate) {
        this.lastLoggedDate = lastLoggedDate;
    }
	public void setPassword(final String password) {
		this.password = password;
	}
    public void setReference(final String reference) {
        this.reference = reference;
    }
    public void setCountry(final String country) {
        this.country = country;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return title +" "+ name +" "+ lastName ;
    }
}