package server.org.appverse.service.rest.sample.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import server.org.appverse.service.rest.sample.SampleBean;

@XmlRootElement
public class Page {

	private List<SampleBean> data;
	private long total;
	private long currentOffset;

	public Page() {
	}

	public Page(final List<SampleBean> data, final int total, final int currentOffset) {
		super();
		this.data = data;
		this.total = total;
		this.currentOffset = currentOffset;
	}

	@XmlElementWrapper
	@XmlElements({ @XmlElement(name = "sampleBean", type = SampleBean.class) })
	/*	
	 * If SampleBean is generic, use this sintax
	 * 
		@XmlElements({ @XmlElement(name = "ledger", type = SampleBean.class),
			@XmlElement(name = "settlement", type = SettlementDTO.class),
			@XmlElement(name = "inventory", type = InventoryDTO.class),
			@XmlElement(name = "statement", type = ClientDocumentDTO.class) })
	*/
	public List<SampleBean> getData() {
		return data;
	}

	public void setData(final List<SampleBean> data) {
		this.data = data;
	}

	public long getCurrentOffset() {
		return currentOffset;
	}

	public void setCurrentOffset(final long currentOffset) {
		this.currentOffset = currentOffset;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(final long total) {
		this.total = total;
	}

}
