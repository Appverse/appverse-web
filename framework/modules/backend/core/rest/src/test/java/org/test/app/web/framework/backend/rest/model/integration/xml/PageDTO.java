/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License 
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this 
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the AppVerse Public License v2.0 
 are met.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) 
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.
 */
package org.test.app.web.framework.backend.rest.model.integration.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.test.app.web.framework.backend.rest.model.integration.SampleDTO;

/**
 * This bean should map paged info and data returned by server.
 * We are using distinct PageDTO objects, since Jackson read Jaxb annotations and It's very difficult to get a generic list page for XML and JSON 
 */
@XmlRootElement(name = "page")
@XmlAccessorType(XmlAccessType.FIELD)
public class PageDTO<Data extends AbstractIntegrationBean> extends AbstractIntegrationBean
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953120036345423159L;
	/**
	 * The remote data.
	 */
	@XmlElementWrapper(name = "data")
	@XmlElements({ @XmlElement(name = "sampleBean", type = SampleDTO.class) })
	/*	
	 * Use this syntax for every generic actual type
	 * 
		@XmlElements({ @XmlElement(name = "sampleBean", type = SampleBean.class),
			@XmlElement(name = "test", type = Test.class),
			@XmlElement(name = "inventory", type = Inventory.class),
			@XmlElement(name = "case", type = Case.class) })
	*/
	private List<Data> dataList;

	@XmlElement(name = "total")
	private int totalSize;

	@XmlElement(name = "currentOffset")
	private int initialPos;

	/**
	 * Creates an empty paging load result bean.
	 */
	public PageDTO() {

	}

	public PageDTO(final List<Data> dataList, final int totalSize, final int initialPos) {
		super();
		this.dataList = dataList;
		this.totalSize = totalSize;
		this.initialPos = initialPos;
	}

	public List<Data> getDataList() {
		return dataList;
	}

	public void setDataList(final List<Data> dataList) {
		this.dataList = dataList;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(final int totalSize) {
		this.totalSize = totalSize;
	}

	public int getInitialPos() {
		return initialPos;
	}

	public void setInitialPos(final int initialPos) {
		this.initialPos = initialPos;
	}
}
