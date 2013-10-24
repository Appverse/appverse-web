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
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This plain bean shows how to change language in a view. Please note that this
 * is just a very plan example to show how to change locale in views. In a real
 * situation you might want this bean to serve just as a controller delegating
 * in your services layer in order to propagate the user selected locale to the
 * server (for instance to keep it in a single Spring session bean holding the
 * minimal user information that has to be stored in the session).
 */
@ManagedBean
@SessionScoped
public class LanguageBean implements Serializable {

	private static final long serialVersionUID = -1476506861433934516L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String localeCode;
	
	public LanguageBean(){
		super();
		localeCode = "en";
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public void changeLanguage(String language) {
		FacesContext.getCurrentInstance().getViewRoot()
				.setLocale(new Locale(language));
		this.localeCode = language;
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