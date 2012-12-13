package org.appverse.web.framework.backend.frontfacade.gxt.services.presentation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface GWTAuthenticationServiceFacadeAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTAuthenticationServiceFacade
     */
    void getAuthorities( AsyncCallback<java.util.List> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTAuthenticationServiceFacade
     */
    void getXSRFSessionToken( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTAuthenticationServiceFacade
     */
    void authenticatePrincipal( java.lang.String p0, java.lang.String p1, AsyncCallback<org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTAuthenticationServiceFacade
     */
    void authenticatePrincipal( java.lang.String p0, java.util.List p1, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTAuthenticationServiceFacade
     */
    void isPrincipalAuthenticated( AsyncCallback<java.lang.Boolean> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTAuthenticationServiceFacade
     */
    void getPrincipal( AsyncCallback<java.lang.String> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static GWTAuthenticationServiceFacadeAsync instance;

        public static final GWTAuthenticationServiceFacadeAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (GWTAuthenticationServiceFacadeAsync) GWT.create( GWTAuthenticationServiceFacade.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "GWTAuthenticationServiceFacade" );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
