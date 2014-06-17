package org.appverse.web.framework.backend.frontfacade.rest.authentication.model;

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
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;


public class JWSAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	//~ Instance fields ================================================================================================

	private final Object principal;

	private final Object payload;

	private final String jwsToken;

	//~ Constructors ===================================================================================================

	/**
	 * This constructor can be safely used by any code that wishes to create a
	 * <code>UsernamePasswordAuthenticationToken</code>, as the {@link
	 * #isAuthenticated()} will return <code>false</code>.
	 *
	 */
	public JWSAuthenticationToken(final String token, final Object payload) {
		super(null);
		this.principal = null;
		this.jwsToken = token;
		this.payload = payload;
	}

	/**
	 * This constructor should only be used by <code>AuthenticationManager</code> or <code>AuthenticationProvider</code>
	 * implementations that are satisfied with producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
	 * authentication token.
	 *
	 * @param principal
	 * @param authorities
	 */
	public JWSAuthenticationToken(final Object principal,
			final Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.jwsToken = null;
		this.payload = null;
		super.setAuthenticated(true); // must use super, as we override
	}

	//~ Methods ========================================================================================================

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}

		super.setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getJwsToken() {
		return jwsToken;
	}

	public Object getPayload() {
		return payload;
	}

}
