package server.org.appverse.service.rest.sample;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SampleBean {
	public int id;
	public int foreignKey;
	public String name;
	public String surname;

	public SampleBean() {
	}

	public SampleBean(final int id, final int foreignKey, final String name, final String surname) {
		super();
		this.id = id;
		this.foreignKey = foreignKey;
		this.name = name;
		this.surname = surname;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		SampleBean that = (SampleBean) o;

		if (id != that.id)
			return false;
		if (foreignKey != that.foreignKey)
			return false;
		if (name != null ? !name.equals(that.name) : that.name != null)
			return false;
		if (surname != null ? !surname.equals(that.surname) : that.surname != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id ^ (id >>> 32);
		result = 31 * result + (foreignKey ^ (foreignKey));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (surname != null ? surname.hashCode() : 0);
		return result;
	}

}
