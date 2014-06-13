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
     * @param smc SOAPMessageContext
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
