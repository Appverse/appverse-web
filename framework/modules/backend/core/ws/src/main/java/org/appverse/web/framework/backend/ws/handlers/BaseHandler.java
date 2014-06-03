package org.appverse.web.framework.backend.ws.handlers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.ws.handler.MessageContext; 

public class BaseHandler<T extends MessageContext> { 
	
	protected String HandlerName = null;
 

	@PostConstruct
	public void init() {  
	}

	@PreDestroy
	public void destroy() {  
	}


	public boolean handleFault(T mc) { 
		return true;
	}
	
	public void close(MessageContext mc) { 
 
	}

	public void setHandlerName(String handlerName) {
		HandlerName = handlerName;
	}
	
}
