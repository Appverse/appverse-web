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
 
/**
* The abstract class will provide the mechanism to connect to the remote service. 
* This method performs the `getPort()` call on the first method call, then it will catch the service instance on the client side on the subsequent calls. 
* That means the client code can decide to call the `getService()` method on the `@PostConstruct` event or on the first request.   
* @author MOCR
*
* @param <S> Service 
* @param <I> Interface
*/
public abstract class AbstractWSClient <S extends Service, I> implements IWSClient<I> { 
    /**
     * Service instance
     */
	private S service;
	/**
	 * Service Interface
	 */
	private I remoteService;
	/**
	 * Service class
	 */
	private Class<S> serviceType;
	/**
	 * Interface type
	 */
	private Class<I> interfaceType; 
	/**
	 * Service handlers list
	 */
	private List<Handler> handlers = new ArrayList<Handler>(); 
	
	public void setBeanClasses(Class<S> serviceType,
			Class<I> interfaceType) {
		this.serviceType = serviceType;
		this.interfaceType = interfaceType;
	} 
	/**
	 * Returns the remote service interface. 
	 * @return service interface
	 */
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
	/**
	 * Adds the Handler to the service Handler Chain.
	 * @param handler
	 */
	public void registerHandler (Handler<?> handler) {
		handlers.add(handler);
	}
	
	/**
	 * Ger the remote WSDL URL String.
	 * @return remote wsdl url as string
	 */
	public abstract String getRemoteWSDLURL ();     

}

