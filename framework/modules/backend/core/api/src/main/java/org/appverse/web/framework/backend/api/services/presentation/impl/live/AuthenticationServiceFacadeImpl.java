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
package org.appverse.web.framework.backend.api.services.presentation.impl.live;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.model.presentation.UserInfoVO;
import org.appverse.web.framework.backend.api.services.presentation.AbstractPresentationService;
import org.appverse.web.framework.backend.api.services.presentation.AuthenticationServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Authentication Service Implementation.
 * It is being used by gwt api in the module initialization phase.
 * @author RRBL
 *
 */

@Service("authenticationServiceFacade")
@Path("authenticationServiceFacade")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationServiceFacadeImpl extends
		AbstractPresentationService implements AuthenticationServiceFacade {

	@Autowired
	private AuthenticationManager authenticationManager;
	

	@Override
	public void authenticatePrincipal(String principal, List<String> credentials) {

		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		User user = new User(principal, "", true, true, true, true, authList);
		Authentication authentication = new PreAuthenticatedAuthenticationToken(
				user, credentials);
		authenticationManager.authenticate(authentication);
	}

	/**
	 * This is a convenience method for making Rest and RPC interfaces of this service compatible.
	 */
	@Override
    @POST
    @Path("authenticatePrincipal")
	@Produces(MediaType.APPLICATION_JSON)
	public AuthorizationDataVO authenticatePrincipal(UserInfoVO userInfo) {
		return authenticatePrincipal(userInfo.getUser(), userInfo.getPassword());
	}
	
	/**
	 * Takes the username and password as provided and checks the validaty of
	 * the credentials. Spring security is used to check the credentielas and to
	 * return the authenticated principal with it's authorized roles. An
	 * exception is thrown if the authentication failes.
	 * 
	 * @param username
	 *            String containing the username of the principal to login
	 * @param password
	 *            String containing the password used to identify the current
	 *            user
	 * @return AuthorizationDataVO object containing the name of the principal
	 *         and the authorized roles.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public AuthorizationDataVO authenticatePrincipal(final String username,
			final String password) {
		final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);
		final Authentication authentication = authenticationManager
				.authenticate(usernamePasswordAuthenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder
				.getContext().getAuthentication().getAuthorities();
		final List<String> grantedRoles = new ArrayList<String>();
		for (final GrantedAuthority grantedAuthority : authorities) {
			grantedRoles.add(grantedAuthority.getAuthority());
		}
		final String name = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		return new AuthorizationDataVO(grantedRoles, name);
	}

	@SuppressWarnings("unchecked")
	@Override
    @POST
    @Path("getAuthorities")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getAuthorities() {
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		List<String> credentials = new ArrayList<String>();
		Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) authentication
				.getAuthorities();
		for (GrantedAuthority grantedAuthority : grantedAuthorities) {
			credentials.add(grantedAuthority.getAuthority());
		}
		return credentials;
	}

	@Override
    @POST
    @Path("getPrincipal")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPrincipal() {
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		return authentication.getName();
	}

	/**
	 * Geneates a cross site request forgivery token, saves it at the current
	 * session and returns the token to the frontend site.
	 * 
	 * This method is intercepted by:
	 * - the spring gwtrpc controller
	 * - the spring json controller
	 * - XSRFCheckFilter
	 * 
	 * Reason for this interception is to decouple these spring beans from the servlet context.
	 * 
	 * @return String object with cross site request forgivery generated token
	 *         for current session
	 * @throws IOException 
	 */
	@Override
    @POST
    @Path("getXSRFSessionToken")
	@Produces(MediaType.APPLICATION_JSON)
	public String getXSRFSessionToken() throws IOException {
		//See Javadoc to understand this method.
		return "";
	}

	/**
	 * Checks if the current user is authenticated by evaluating the
	 * SecurityContext
	 * 
	 * @return Boolean true if the current user is authenticated and false
	 *         elsewise
	 */
	@Override
    @POST
    @Path("isPrincipalAuthenticated")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean isPrincipalAuthenticated() {
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		if (authentication != null) {
			final UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
			return userPrincipal != null;
		} else {
			return false;
		}

	}

}
