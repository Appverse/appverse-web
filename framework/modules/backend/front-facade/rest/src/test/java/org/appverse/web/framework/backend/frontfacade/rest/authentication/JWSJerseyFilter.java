package org.appverse.web.framework.backend.frontfacade.rest.authentication;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.business.CertService;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.business.impl.live.CertServiceImpl;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.filter.JWSAuthenticationProcessingFilter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glassfish.jersey.message.MessageBodyWorkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.security.Key;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;

@Component
@Provider
public class JWSJerseyFilter implements ClientRequestFilter, ClientResponseFilter {

	private static Logger logger = LoggerFactory.getLogger(JWSJerseyFilter.class);

	private final CertService certService = new CertServiceImpl(logger);

	@Context
	private MessageBodyWorkers workers;

	@Override
	//RequestFilter
	public void filter(final ClientRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub

		try
		{
			/*

			//It doesn't work

			// use ServiceLocatorClientProvider to extract HK2 ServiceLocator from request
			final ServiceLocator locator = ServiceLocatorClientProvider
					.getServiceLocator(requestContext);

			// and ask for MyInjectedService:
			CertService certService = locator.getService(CertService.class);
			*/

			KeyStore keyStore = certService.getKeyStore("certificates/client/client.pfx", "PKCS12",
					"export");
			String keyPassword = "export";
			Key key = keyStore.getKey("client.restservices.gbs.db.com", keyPassword.toCharArray());

			//Create RSA-signer with the private key
			JWSSigner signer = new RSASSASigner((RSAPrivateKey) key);
            signer.setProvider(new BouncyCastleProvider());
			/*
			// RSA signatures require a public and private RSA key pair,
			// the public key must be made known to the JWS recipient in
			// order to verify the signatures
			 */
			// Prepare JWS object with simple string as payload

			String reqUri = requestContext.getUri().toString();
			if (logger.isDebugEnabled())
				logger.debug("URI:: " + reqUri);

			// buffer into which myBean will be serialized
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			Object object = requestContext.getEntity();

			Payload objectPay = null;
			if (object != null)
			{
				Class<Object> type = (Class<Object>) requestContext.getEntityClass();
				GenericType<Object> genericType = new GenericType<Object>(type) {
				};

				// get most appropriate MBW
				final MessageBodyWriter<Object> messageBodyWriter =
						workers.getMessageBodyWriter(type, type,
								new Annotation[] {}, MediaType.APPLICATION_JSON_TYPE);

				try {
					// use the MBW to serialize myBean into baos
					messageBodyWriter.writeTo(object,
							object.getClass(), genericType.getType(), new Annotation[] {},
							MediaType.APPLICATION_JSON_TYPE,
							new MultivaluedHashMap<String, Object>(),
							baos);
				} catch (IOException e) {
					throw new RuntimeException(
							"Error while serializing MyBean.", e);
				}

				String stringJsonOutput = baos.toString();
				if (logger.isDebugEnabled())
					logger.debug("Entity.toString():: " + stringJsonOutput);

				//There is a short limitation for http headers. It depends on server.
				//As message payload grows, payload in header is growing too, so we must set a limit.
				//It means that we are only signing, validating and checking message integrity of first 1024 characters
				//Same logic is applied in server side
				if (stringJsonOutput != null
						&& stringJsonOutput.length() > JWSAuthenticationProcessingFilter.PAYLOAD_HEADER_MAX_SIZE)
					stringJsonOutput = stringJsonOutput.substring(0,
							JWSAuthenticationProcessingFilter.PAYLOAD_HEADER_MAX_SIZE);

				objectPay = new Payload(stringJsonOutput);
			}
			//If request entity is null (usually GET methods) use URI as payload
			else
				objectPay = new Payload(reqUri);

			JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.RS256), objectPay);

			// Compute the RSA signature
			jwsObject.sign(signer);
			String s = jwsObject.serialize();

			if (logger.isDebugEnabled())
				logger.debug("serialized compact form: " + s);

			requestContext.getHeaders().add("Authorization", "Bearer " + s);
		} catch (Exception e)
		{
			logger.error("Error signing message", e);
			requestContext.abortWith(
					Response.status(Response.Status.BAD_REQUEST).entity("Error signing message")
							.build());
		}

	}

	@Override
	//ResponseFilter
	public void filter(final ClientRequestContext requestContext,
			final ClientResponseContext responseContext)
			throws IOException {
		// TODO Auto-generated method stub

	}

	private class InterceptorStream extends OutputStream {
		private final StringBuilder b;
		private final OutputStream inner;
		private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InterceptorStream(final StringBuilder b, final OutputStream inner) {
			this.b = b;
			this.inner = inner;
		}

		StringBuilder getStringBuilder() {
			// write entity to the builder
			final byte[] entity = baos.toByteArray();

			b.append(new String(entity, 0, entity.length));
			b.append('\n');

			return b;
		}

		@Override
		public void write(final int i) throws IOException {
			baos.write(i);
			inner.write(i);
		}
	}

}
