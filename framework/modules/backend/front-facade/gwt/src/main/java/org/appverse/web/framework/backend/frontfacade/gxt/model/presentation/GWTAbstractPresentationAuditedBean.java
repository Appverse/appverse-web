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
package org.appverse.web.framework.backend.frontfacade.gxt.model.presentation;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public abstract class GWTAbstractPresentationAuditedBean extends
		GWTAbstractPresentationBean implements IsSerializable {

	private static final long serialVersionUID = -3192415836684736174L;

	protected Date created;

	protected String createdBy;

	protected Date updated;

	protected String updatedBy;
	protected String status;
	protected long version;

	public static final String STATUS_ACTIVE = "a";
	public static final String STATUS_INACTIVE = "i";

	public Date getCreated() {
		return created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getStatus() {
		return status;
	}

	public Date getUpdated() {
		return updated;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public long getVersion() {
		return version;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}