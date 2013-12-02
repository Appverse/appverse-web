package server.org.appverse.service.rest.sample.json;

import java.util.List;

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
