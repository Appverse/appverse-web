package org.test.app.web.framework.backend.rest.model.integration;

import javax.xml.bind.annotation.XmlRootElement;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;

@XmlRootElement(name = "sampleBean")
public class SampleDTO extends AbstractIntegrationBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9092781558512031601L;

	private int id;
	private int foreignKey;
	private String name;
	private String surname;

	public SampleDTO(final int id, final int foreignKey, final String name, final String surname) {
		super();
		this.id = id;
		this.foreignKey = foreignKey;
		this.name = name;
		this.surname = surname;
	}

	public SampleDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(final int foreignKey) {
		this.foreignKey = foreignKey;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

}
