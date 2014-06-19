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
package org.appverse.web.framework.backend.batch.model.integration;

import org.appverse.web.framework.backend.persistence.model.integration.AbstractIntegrationAuditedJPABean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the BATCH_NODE database table.
 * 
 */
@Entity
@Table(name = "BATCH_NODE")
public class BatchNodeDTO extends AbstractIntegrationAuditedJPABean implements
		Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String active;
	private Date created;
	private String createdBy;
	private String name;
	private String status;
	private Date updated;
	private String updatedBy;
	private long version;

	public BatchNodeDTO() {
	}

	@Column(nullable = false, length = 1)
	public String getActive() {
		return active;
	}

	@Override
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	public Date getCreated() {
		return created;
	}

	@Override
	@Column(name = "CREATED_BY", nullable = false)
	public String getCreatedBy() {
		return createdBy;
	}

	@Id
	@SequenceGenerator(name = "BATCH_NODE_ID_GENERATOR", sequenceName = "SEQ_BATCH_NODE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BATCH_NODE_ID_GENERATOR")
	@Column(unique = true, nullable = false, precision = 20)
	public long getId() {
		return id;
	}

	@Column(nullable = false, length = 100)
	public String getName() {
		return name;
	}

	@Override
	@Column(nullable = false, length = 1)
	public String getStatus() {
		return status;
	}

	@Override
	@Temporal(TemporalType.DATE)
	public Date getUpdated() {
		return updated;
	}

	@Override
	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}

	@Override
	@Column(name = "\"VERSION\"", nullable = false, precision = 20)
	public long getVersion() {
		return version;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public void setVersion(long version) {
		this.version = version;
	}

}