package org.appverse.web.framework.backend.ws.handlers;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SOAP Handler: Log the whole SOAP inbound / outbound messages
 * 
 **/
public class EnvelopeLoggingSOAPHandler extends BaseHandler<SOAPMessageContext> implements SOAPHandler<SOAPMessageContext> {
    /**
     * Logger
     */
	private static Logger logger = LoggerFactory.getLogger(EnvelopeLoggingSOAPHandler.class);
	/**
	 * HANDLER name
	 */
	private static final String HANDLER_NAME = "EnvelopeLoggingSOAPHandler";
    /**
     * Constructor 
     */
    public EnvelopeLoggingSOAPHandler(){
		super();
		super.setHandlerName(HANDLER_NAME);		
    }
    /**
     * Handle message 
     * @param SOAPMessageContext
     */
    public boolean handleMessage(SOAPMessageContext smc) { 
		logToSystemOut(smc); 
        return true;
    }
    /**
     * Log message
     * @param smc SOAPMessageContext
     */
    private void logToSystemOut(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean)
            smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        
        if (outboundProperty.booleanValue()) {
        	logger.info("Outbound: ");
        } else {
        	logger.info("Inbound: ");
        }
        
        SOAPMessage message = smc.getMessage();
        try {
        	ByteArrayOutputStream outString = new ByteArrayOutputStream();
      		message.writeTo(outString);		
            String strMsg = new String(outString.toByteArray());
            logger.info("" + strMsg);  
        } catch (Exception e) {
            logger.error("Exception in SOAP Handler #1: " + e);
        }
    }
    /**
     * Get message headers.
     * Not implemented.
     */
	public Set<QName> getHeaders() {
		return null;
	}
}
