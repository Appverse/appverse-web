package org.appverse.web.framework.backend.ws.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;
 

public abstract class AbstractWSClient <S extends Service, I> implements IWSClient<I> { 
  
	private S service;
	private I remoteService;
	
	private Class<S> serviceType;
	private Class<I> interfaceType; 
	
	private List<Handler> handlers = new ArrayList<Handler>(); 
	
	public void setBeanClasses(Class<S> serviceType,
			Class<I> interfaceType) {
		this.serviceType = serviceType;
		this.interfaceType = interfaceType;
	} 
	 
	public I getService() throws Exception {
		try {  
		if (remoteService == null) { 
			WebServiceClient ann = serviceType.getAnnotation(WebServiceClient.class);			 	 
			URL remoteWsdl = new URL( getRemoteWSDLURL()); 
			service = serviceType.getConstructor(URL.class, QName.class).newInstance(remoteWsdl, new QName(ann.targetNamespace(), ann.name()));  
			remoteService = service.getPort(interfaceType);   
			if (handlers.size() > 0 ) {				
				BindingProvider bindProv = (BindingProvider) remoteService; 
				bindProv.getBinding().setHandlerChain(handlers); 
			}
		}
		} catch (WebServiceException ex) {
			throw new Exception ("Service connection fails.");
		} catch (NullPointerException e) {
			throw new Exception ("Review JaxWSClient configuration.");
		}
		return remoteService;
	}
	
	public void registerHandler (Handler<?> handler) {
		handlers.add(handler);
	}
	
	 public abstract String getRemoteWSDLURL ();     

}

