package org.appverse.web.framework.backend.rest.filters.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glassfish.jersey.message.MessageBodyWorkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.security.interfaces.RSAPrivateKey;

@Component
@Provider
public class JWSJerseyFilter implements ClientRequestFilter {

    public static final String JWS_FILTER_KEY="jws.filter.key";
    public static final String JWS_AUTHORIZATION_HEADER="Authorization";
    public static final String JWS_AUTHORIZATION_START_TOKEN="Bearer ";

	private static Logger logger = LoggerFactory.getLogger(JWSJerseyFilter.class);

    public static final int PAYLOAD_HEADER_MAX_SIZE = 1024;
    private static RSAPrivateKey key = null;



    public JWSJerseyFilter(){

    }
    public JWSJerseyFilter(RSAPrivateKey key){
        this.key = key;
    }

	@Context
	private MessageBodyWorkers workers;

	@Override
    /**
     * RSA signatures require a public and private RSA key pair, the public key must be made known to the JWS recipient in order to verify the signatures
    **/
	public void filter(final ClientRequestContext requestContext) throws IOException {
        final Configuration config = requestContext.getConfiguration();

        //filter only valid on client-side
        if (RuntimeType.CLIENT != config.getRuntimeType()) {
            return;
        }
        //tries to obtain a key
        checkParams(requestContext);

        //Create RSA-signer with the private key
        JWSSigner signer = null;
        if (key != null) {
            signer = new RSASSASigner(key);
            signer.setProvider(new BouncyCastleProvider());

        } else {
            requestContext.abortWith(
                    Response.status(Response.Status.BAD_REQUEST).entity("Error invalid " + JWS_FILTER_KEY + " param. Should be a valid RSAPrivateKey")
                            .build()
            );
            return;
        }

        if (logger.isDebugEnabled()) {

            logger.debug("URI:: " + getUri(requestContext));
        }

        Payload objectPay = obtainObjectPay(requestContext);

        try
		{
            // Prepare JWS object with simple string as payload
			JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.RS256), objectPay);

			// Compute the RSA signature
			jwsObject.sign(signer);
			String s = jwsObject.serialize();

			if (logger.isDebugEnabled())
				logger.debug("serialized compact form: " + s);

            //add a header with computed signature
			requestContext.getHeaders().add(JWS_AUTHORIZATION_HEADER, JWS_AUTHORIZATION_START_TOKEN + s);
		} catch (Exception e)
		{
			logger.error("Error signing message", e);
			requestContext.abortWith(
					Response.status(Response.Status.BAD_REQUEST).entity("Error signing message")
							.build());

        }

	}

    /**
     * Check parameters on request Context
     * @param requestContext
     */
    private void checkParams(ClientRequestContext requestContext){
        if (key==null) {
            Object keyObject = requestContext.getProperty(JWS_FILTER_KEY);
            if (keyObject == null) {
                requestContext.abortWith(
                        Response.status(Response.Status.BAD_REQUEST).entity("Error " + JWS_FILTER_KEY + " param is required")
                                .build()
                );
            }else if (keyObject instanceof RSAPrivateKey) {
                key = (RSAPrivateKey)keyObject;

           }

        }
    }

    /**
     * Generates a payload based on request context. On empty content use url as content.
     * @param requestContext
     * @return payload based on content
     */
    private Payload obtainObjectPay(ClientRequestContext requestContext){
        Payload objectPay = null;

        Object object = requestContext.getEntity();
        if (object != null)
        {
            // buffer into which myBean will be serialized
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
                    && stringJsonOutput.length() > PAYLOAD_HEADER_MAX_SIZE)
                stringJsonOutput = stringJsonOutput.substring(0, PAYLOAD_HEADER_MAX_SIZE);

            objectPay = new Payload(stringJsonOutput);
        }
        //If request entity is null (usually GET methods) use URI as payload
        else {
            String reqUri = getUri(requestContext);
            objectPay = new Payload(reqUri);
        }
        return objectPay;
    }
    private String getUri(ClientRequestContext requestContext){
        URI uri = requestContext.getUri();
        return uri!=null?uri.getPath():null;
    }

    public MessageBodyWorkers getWorkers() {
        return workers;
    }

    public void setWorkers(MessageBodyWorkers workers) {
        this.workers = workers;
    }
}
