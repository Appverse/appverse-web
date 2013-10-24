/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Mozilla Public 
 License, v. 2.0. If a copy of the MPL was not distributed with this 
 file, You can obtain one at http://mozilla.org/MPL/2.0/. 

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the Mozilla Public License v2.0 
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
package org.appverse.web.showcases.jsf2showcase.backend.model.presentation;

import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationAuditedBean;

public class UserVO extends AbstractPresentationAuditedBean {

	private static final long serialVersionUID = 1L;

	private long id;

	@Size(min = 4, max = 10, message = "{validator.username.length}")
	@NotNull(message = "{validator.username.required}")
	private String name;

	@Size(min = 4, max = 10, message = "{validator.lastname.length}")
	@NotNull(message = "{validator.lastname.required}")
	private String lastName;

	@Size(min = 10, max = 20, message = "{validator.email.length}")
	@NotNull(message = "{validator.email.required}")
	private String email;

	@Size(min = 4, max = 40, message = "{validator.password.length}")
	@NotNull(message = "{validator.password.required}")
	private String password;
	
	private String signup;

	@NotNull
	private boolean active = true;
	
	@NotNull
	private Date initDate;

	@NotNull
	private Date endDate;

	@AssertTrue(message = "{validator.inidnidate.invalid}")
	public boolean isCheckDates() {
		if (endDate.before(initDate)) {
			return false;
		}
		return true;
	}	

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public UserVO() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getSignup() {
		return signup;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setSignup(final String signup) {
		this.signup = signup;
	}
}
