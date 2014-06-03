package org.appverse.web.framework.backend.ws.client;
/**
 * JAX WS Client interface
 * @author MOCR
 * 
 * @param <I> Service interface
 */
public interface IWSClient<I> {
	/**
	 * Returns the remote service interface. 
	 * @return service interface
	 */
	public I getService() throws Exception;

}
