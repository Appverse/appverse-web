package org.appverse.web.framework.backend.frontfacade.rest.authentication;
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
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.business.CertService;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.model.JWSAuthenticationToken;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;


/**
 * JWS AuthenticationProvider
 *
 *
 *   * certificatePath a path to server public certificate.
 *   * defaultRoles a comma separated string containing the roles
 */
public class JWSAuthenticationProvider implements AuthenticationProvider, MessageSourceAware,InitializingBean {

	@AutowiredLogger
	private Logger logger;

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private UserDetailsService userDetailsService;

    private Resource certificatePath;

    private String defaultRoles="ROLE_REST_USER";

    //loaded on initialization
    private JWSVerifier verifier = null;
    private String cn = null;

	@Autowired
    private CertService certService;

	@Override
	public Authentication authenticate(final Authentication authentication)
			throws AuthenticationException {

		JWSAuthenticationToken authRequest = (JWSAuthenticationToken) authentication;
		String token = authRequest.getJwsToken();
		Object messagePayload = authRequest.getPayload();
		if (StringUtils.isEmpty(token))
			throw new BadCredentialsException("Auth Token invalid");



		try
		{
			JWSObject jwsObject = JWSObject.parse(token);

            //We should test this comparation with binary payloads
            //Ensure message integrity
            if (!jwsObject.getPayload().toString()
                    .equals(messagePayload.toString())) {
                throw new BadCredentialsException("Invalid payload");
            }

			if (jwsObject.verify(verifier))
			{
				Collection<GrantedAuthority> authoritiesDefault = new ArrayList<GrantedAuthority>();
                String[] roles = defaultRoles.split(",");
                for (String role : roles) {
                    if (!StringUtils.isEmpty(role)){
                        GrantedAuthority auth = new SimpleGrantedAuthority(defaultRoles);
                        authoritiesDefault.add(auth);
                    }
                }

				if (userDetailsService != null)
				{
					UserDetails userDetails = userDetailsService.loadUserByUsername(cn);
					authoritiesDefault.addAll(userDetails.getAuthorities());
				}

				JWSAuthenticationToken authResult =
						new JWSAuthenticationToken((Object) cn, authoritiesDefault);

				if (logger.isDebugEnabled()) {
					logger.debug("CN: " + cn);
					logger.debug("Authentication success: " + authResult);
				}

				return authResult;
			}
		} catch(ParseException pe){
            throw new BadCredentialsException("Invalid JWS Object", pe);
        } catch (UsernameNotFoundException unfe){
			throw new BadCredentialsException("Auth Token invalid", unfe);
		}catch(Exception e){
            throw new BadCredentialsException("Unknown error", e);
        }
		return null;

	}

	@Override
	public void setMessageSource(final MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return JWSAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public void setUserDetailsService(final UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	protected UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

    public Resource getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(Resource certificatePath) {
        this.certificatePath = certificatePath;
    }

    /**
     * Tries to load the client certificate on initialization
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.certService == null ){
            logger.error("Invalid configuration: CertService");
            throw new BeanCreationException("Invalid configuration, CertService not found");
        }
        if (certificatePath==null) {
            logger.error("Invalid configuration: certificate Path not found");
            throw new BeanCreationException("Invalid configuration, certificatePath not found");
        }

        try {
            X509Certificate cert = certService.getCertificateFromInput(certificatePath.getInputStream());

            cn = certService.getName(cert);

            PublicKey publicKey = cert.getPublicKey();

            verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
            verifier.setProvider(new BouncyCastleProvider());


        } catch (Exception exc) {
            logger.error("Couldn't instantiate X.509 certificate", exc);
            throw   new BeanCreationException("Invalid configuration, certificatePath not found", exc);
        }

    }

    public String getDefaultRoles() {
        return defaultRoles;
    }

    public void setDefaultRoles(String defaultRoles) {
        this.defaultRoles = defaultRoles;
    }
}
