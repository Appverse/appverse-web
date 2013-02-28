package ${package}.backend.model.integration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.appverse.web.framework.backend.persistence.model.integration.AbstractIntegrationAuditedJPABean;

@Entity
@Table(name = "USER")
public class UserDTO extends AbstractIntegrationAuditedJPABean implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String lastName;
	private String email;
	private String password;

	private boolean active = true;


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

	@Column(nullable = false, length = 40)
	public String getLastName() {
		return lastName;
	}

	@Column(nullable = false, length = 40)
	public String getName() {
		return name;
	}

	@Column(nullable = false, length = 40)
	public String getPassword() {
		return password;
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

	public void setActive(final boolean active) {
		this.active = active;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
}