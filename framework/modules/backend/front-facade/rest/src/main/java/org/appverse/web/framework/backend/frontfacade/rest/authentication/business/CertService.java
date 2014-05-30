package org.appverse.web.framework.backend.frontfacade.rest.authentication.business;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface CertService {
	public X509Certificate getCertificateFromInput(final InputStream inStream);

	/* (non-Javadoc)
	 * @see com.gft.ugh.service.cert.ICertService#getKeyStore(java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * PKCS12 KeyStore
	 */
	public KeyStore getKeyStore(final String keystore, final String keystoreType,
                                final String keystorePass) throws Exception;

	/* (non-Javadoc)
	 * @see com.gft.ugh.service.cert.ICertService#getCertificateChain(java.security.KeyStore)
	 */
	public X509Certificate[] getCertificateChain(final KeyStore ks0) throws Exception;

	/* (non-Javadoc)
	 * @see com.gft.ugh.service.cert.ICertService#getPrivateKey(java.security.KeyStore, char[])
	 */
	public PrivateKey getPrivateKey(final KeyStore ks0, final char[] pass) throws Exception;

	/**
	 * Retrieves the name for the given certificate.
	 * 
	 * @param certificate the certificate to get its name for, cannot be <code>null</code>.
	 * @return the name for the given certificate, can be <code>null</code>.
	 */
	public String getName(final X509Certificate certificate);

}
