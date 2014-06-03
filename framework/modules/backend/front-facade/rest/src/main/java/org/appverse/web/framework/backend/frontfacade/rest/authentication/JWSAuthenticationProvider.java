package org.appverse.web.framework.backend.frontfacade.rest.authentication;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.business.CertService;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.model.JWSAuthenticationToken;
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

public class JWSAuthenticationProvider implements AuthenticationProvider, MessageSourceAware,InitializingBean {

	@AutowiredLogger
	private Logger logger;

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private UserDetailsService userDetailsService;

    private Resource certificatePath;

    //loaded on initialization
    private JWSVerifier verifier = null;
    private String cn = null;

	@Autowired
    CertService certService;

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

			if (jwsObject.verify(verifier))
			{
				Collection<GrantedAuthority> authoritiesDefault = new ArrayList<GrantedAuthority>();
				GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_REST_USER");
				authoritiesDefault.add(auth);

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

        } catch (Exception exc) {
            logger.error("Couldn't instantiate X.509 certificate", exc);
            throw   new BeanCreationException("Invalid configuration, certificatePath not found", exc);
        }

    }
}
