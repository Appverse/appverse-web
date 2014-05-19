package org.appverse.web.framework.backend.frontfacade.rest.authentication.business.impl.live;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.business.CertService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Component("certService")
public class CertServiceImpl implements CertService
{

	@AutowiredLogger
	private Logger logger;

	public CertServiceImpl()
	{
	}

	public CertServiceImpl(final Logger logger)
	{
		this.logger = logger;
	}

	//------  Get recipient certificate from file -------------
	/* Encoded X509 (.cer )
	 * @see com.gft.ugh.service.cert.ICertService#getCertificateFromFile(java.io.InputStream)
	 */
	@Override
	public X509Certificate getCertificateFromInput(final InputStream inStream)
	{
		X509Certificate cert = null;
		try
		{
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate) cf.generateCertificate(inStream);
			inStream.close();
		} catch (Exception exc)
		{
			logger.error("Couldn't instantiate X.509 certificate", exc);
		}
		return cert;
	}

	/* (non-Javadoc)
	 * @see com.gft.ugh.service.cert.ICertService#getKeyStore(java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * PKCS12 KeyStore
	 */
	@Override
	public KeyStore getKeyStore(final String keystore, final String keystoreType,
			final String keystorePass)
			throws Exception
	{
		KeyStore keyStore = null;

		try
		{
			keyStore = KeyStore.getInstance(keystoreType);
			if (logger.isDebugEnabled())
				logger.debug("KeyStore:: " + keystore);
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(keystore);

			keyStore.load(in, keystorePass.toCharArray());
		} catch (Exception e)
		{
			logger.error("It hasn't been possible to load keystore.", e);
			throw new Exception(e);
		}
		return keyStore;
	}

	/* (non-Javadoc)
	 * @see com.gft.ugh.service.cert.ICertService#getCertificateChain(java.security.KeyStore)
	 */
	@Override
	public X509Certificate[] getCertificateChain(final KeyStore ks0) throws Exception
	{
		X509Certificate[] certs = null;
		Enumeration<?> en = ks0.aliases();
		Certificate[] tempCertChain = null;
		while (en.hasMoreElements() && tempCertChain == null)
		{
			String alias = (String) en.nextElement();
			if (ks0.isKeyEntry(alias))
				tempCertChain = ks0.getCertificateChain(alias);
		}
		certs = new X509Certificate[tempCertChain.length];
		for (int i = 0; i < tempCertChain.length; i++)
			certs[i] = (X509Certificate) tempCertChain[i];

		return certs;
	}

	/* (non-Javadoc)
	 * @see com.gft.ugh.service.cert.ICertService#getPrivateKey(java.security.KeyStore, char[])
	 */
	@Override
	public PrivateKey getPrivateKey(final KeyStore ks0, final char[] pass) throws Exception
	{
		String keyAlias = null; // Alias for public key certificate corresponding to private key
		Enumeration<?> en = ks0.aliases();

		while (en.hasMoreElements())
		{
			String temp = (String) en.nextElement();

			if (ks0.isKeyEntry(temp))
				keyAlias = temp;
		}
		// Obtaining private key as PrivateKey class
		return (PrivateKey) ks0.getKey(keyAlias, pass);
	}

	/**
	 * Retrieves the name for the given certificate.
	 * 
	 * @param certificate the certificate to get its name for, cannot be <code>null</code>.
	 * @return the name for the given certificate, can be <code>null</code>.
	 */
	@Override
	public String getName(final X509Certificate certificate) {
		try {
			String dn = certificate.getSubjectX500Principal().getName();
			if ("dn".equalsIgnoreCase("cn")) {
				return dn;
			}

			LdapName ldapDN = new LdapName(dn);
			for (Rdn rdn : ldapDN.getRdns()) {
				if ("cn".equalsIgnoreCase(rdn.getType())) {
					return (String) rdn.getValue();
				}
			}
		} catch (InvalidNameException e) {
			// Ignore...
		}
		return null;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(final Logger logger) {
		this.logger = logger;
	}

}
