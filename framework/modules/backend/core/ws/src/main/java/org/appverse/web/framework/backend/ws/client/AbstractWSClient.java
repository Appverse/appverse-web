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

