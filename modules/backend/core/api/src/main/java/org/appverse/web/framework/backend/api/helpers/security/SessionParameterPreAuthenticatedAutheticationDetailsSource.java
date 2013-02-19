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
package org.appverse.web.framework.backend.api.helpers.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.MappableAttributesRetriever;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;

public class SessionParameterPreAuthenticatedAutheticationDetailsSource extends
		J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource {
	private String authoritiesSessionAttribute = "REMOTE_AUTHORITIES";

	@Override
	@SuppressWarnings("unchecked")
	public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
			HttpServletRequest request) {
		if (request.getSession().getAttribute(authoritiesSessionAttribute) != null) {
			List<String> authorities = (List<String>) request.getSession()
					.getAttribute(authoritiesSessionAttribute);
			Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			for (String authority : authorities) {
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
						authority);
				grantedAuthorities.add(grantedAuthority);
			}
			PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails details = new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(
					request, grantedAuthorities);
			return details;
		} else {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
					"NO_AUTHORITY");
			Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			grantedAuthorities.add(grantedAuthority);
			PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails details = new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(
					request, grantedAuthorities);
			return details;
		}
	}

	public void setAuthoritiesSessionAttribute(
			String authoritiesSessionAttribute) {
		this.authoritiesSessionAttribute = authoritiesSessionAttribute;
	}

	@Override
	public void setMappableRolesRetriever(
			MappableAttributesRetriever aJ2eeMappableRolesRetriever) {
		this.j2eeMappableRoles = new HashSet<String>();
	}

}
