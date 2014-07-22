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
package org.appverse.web.framework.backend.api.services.presentation.impl.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.model.presentation.UserInfoVO;
import org.appverse.web.framework.backend.api.services.presentation.AbstractPresentationService;
import org.appverse.web.framework.backend.api.services.presentation.AuthenticationServiceFacade;
import org.springframework.stereotype.Service;

@Service("authenticationServiceFacade")
public class AuthenticationServiceFacadeMockImpl extends AbstractPresentationService implements
		AuthenticationServiceFacade {

	@Override
	public void authenticatePrincipal(String username, List<String> authorities) {
	}

	/**
	 * Takes the username and password as provided and checks the validaty of
	 * the credentials. return the authenticated principal with it's authorized
	 * roles.
	 * 
	 * @param username
	 *            String containing the username of the principal to login
	 * @param password
	 *            String containing the password used to identify the current
	 *            user
	 * @return AuthorizationDataFO object containing the name of the principal
	 *         and the authorized roles.
	 */
	@Override
	public AuthorizationDataVO authenticatePrincipal(final String username, final String password) {

		final List<String> grantedRoles = new ArrayList<String>();
		grantedRoles.add("USER");

		return new AuthorizationDataVO(grantedRoles, username);
	}

	@Override
	public List<String> getAuthorities() {
		return null;
	}

	@Override
	public String getPrincipal() {
		return null;
	}

	/**
	 * Geneates a cross site request forgivery token, saves it at the current
	 * session and returns the token to the frontend site.
	 * 
	 * This method is intercepted by the spring gwtrpc controller
	 * 
	 * @return String object with cross site request forgivery generated token
	 *         for current session
	 */
	@Override
	public String getXSRFSessionToken() throws IOException {

		return "";
	}

	/**
	 * Checks if the current user is authenticated
	 * 
	 * @return Boolean true if the current user is authenticated and false
	 *         elsewise
	 */
	@Override
	public boolean isPrincipalAuthenticated() {
		return true;
	}

	@Override
	public AuthorizationDataVO authenticatePrincipal(UserInfoVO userInfo) {
		return authenticatePrincipal(userInfo.getUser(), userInfo.getPassword());
	}
}
