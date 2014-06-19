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
package server.org.appverse.service.rest.sample.json;

import server.org.appverse.service.rest.sample.SampleBean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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

	@Override
	public int hashCode() {
		int result = (int) (total ^ (total >>> 32));
		result = 31 * result + (int) ((currentOffset ^ (currentOffset)));
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}
}
