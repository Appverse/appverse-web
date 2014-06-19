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
package org.appverse.web.framework.backend.rest.model.integration;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;

import java.io.Serializable;
import java.util.List;

/**
 * Page of data which contains also pagination information 
 * 
 */
public class IntegrationPaginatedResult<Data extends AbstractIntegrationBean>
		extends AbstractIntegrationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8953120036156423159L;
	/**
	 * The remote data.
	 */
	private List<Data> data;
	private int totalLength;
	private int offset;

	/**
	 * Creates an empty paging load result bean.
	 */
	public IntegrationPaginatedResult() {

	}

	/**
	 * Creates a new paging list load result.
	 * 
	 * @param list
	 *            the data
	 * @param totalLength
	 *            the total length
	 * @param offset
	 *            the paging offset
	 */
	public IntegrationPaginatedResult(final List<Data> data, final int totalLength,
			final int offset) {
		this.data = data;
		this.totalLength = totalLength;
		this.offset = offset;
	}

	public List<Data> getData() {
		return data;
	}

	public int getOffset() {
		return offset;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setData(final List<Data> data) {
		this.data = data;
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public void setTotalLength(final int totalLength) {
		this.totalLength = totalLength;
	}
}
