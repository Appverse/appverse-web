package org.appverse.web.framework.backend.ws.handlers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.ws.handler.MessageContext; 
/**
 * Base Handler class
 * @author MOCR
 *
 * @param <T> Type
 */
public class BaseHandler<T extends MessageContext> { 
	/**
	 * Handler name
	 */
	protected String HandlerName = null;
 
	/**
	 * init
	 */
	@PostConstruct
	public void init() {  
	}
	/**
	 * Destroy
	 */
	@PreDestroy
	public void destroy() {  
	}

	/**
	 * Handle Fault
	 * @param mc
	 * @return
	 */
	public boolean handleFault(T mc) { 
		return true;
	}
	/**
	 * Close
	 * @param mc
	 */
	public void close(MessageContext mc) { 
 
	}
	/**
	 * Set Handler name
	 * @param handlerName
	 */
	public void setHandlerName(String handlerName) {
		HandlerName = handlerName;
	}
	
}
