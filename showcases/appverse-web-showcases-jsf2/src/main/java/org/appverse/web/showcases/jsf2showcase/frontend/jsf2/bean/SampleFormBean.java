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
package org.appverse.web.showcases.jsf2showcase.frontend.jsf2.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@RequestScoped
public class SampleFormBean implements Serializable {

	private static final long serialVersionUID = 6558426905322907164L;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String username;
	private String password;
	private Boolean connDB;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getConnDB() {
		return connDB;
	}

	public void setConnDB(Boolean connDB) {
		this.connDB = connDB;
	}

	public String enterPage() {
		try {
			if (!connDB.booleanValue()) {
				throw new Exception("Database error");
			}
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.getExternalContext().redirect(
					"errGeneratorChck.jsf?url=success.xhtml");
			facesContext.renderResponse();
			return "";

		} catch (Exception e) {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Error connecting to Database", null);
			facesContext.addMessage(null, message);

			return "";

		}
	}

	@PostConstruct
	public void initIt() {		
		logger.info("*** BEAN POST CONSTRUCTION: " + this.getClass().toString());
	}

	@PreDestroy
	public void cleanUp() {
		logger.info("*** BEAN POST DESTRUCTION: " + this.getClass().toString());
	}
}
