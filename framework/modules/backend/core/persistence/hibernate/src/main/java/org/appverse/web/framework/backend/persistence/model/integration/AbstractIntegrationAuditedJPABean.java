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
package org.appverse.web.framework.backend.persistence.model.integration;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationAuditedBean;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractIntegrationAuditedJPABean extends
		AbstractIntegrationAuditedBean {

	private static final long serialVersionUID = -2070164067618480118L;
	protected long id;

	// Always add condition "if (id == 0 || id != other.id)" so in case that
	// Dozer non-cummulative collections and remove-orphans in mappings works
	// fine
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractIntegrationAuditedJPABean other = (AbstractIntegrationAuditedJPABean) obj;
		if (id == 0 || id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public void finalize() throws Throwable {

	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreated() {
		return created;
	}

	@Override
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	@Column(nullable = false)
	public String getStatus() {
		return status;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdated() {
		return updated;
	}

	@Override
	@Column(name = "UPDATED_BY", length = 100)
	public String getUpdatedBy() {
		return updatedBy;
	}

/* Difference with EclipseLink
   This does not work for Hibernate as it manages override of annotations in JPA entities different than Eclipselink.
   Overriding this field and adding @Version for optimistic locking would not work (it does with EclipseLink).
   For optimistic locking with Hibernate this has to be added directly in the project classes.
	@Override
	@Column(nullable = false)
	public long getVersion() {
		return version;
	}
*/

	// Required so that Dozer non-cummulative collections and remove-orphans in
	// mappings works fine
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ id >>> 32);
		return result;
	}

	public void setId(long id) {
		this.id = id;
	}
}