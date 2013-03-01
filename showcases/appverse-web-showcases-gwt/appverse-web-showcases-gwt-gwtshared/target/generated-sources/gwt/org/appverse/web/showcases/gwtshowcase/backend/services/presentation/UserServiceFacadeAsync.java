package org.appverse.web.showcases.gwtshowcase.backend.services.presentation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface UserServiceFacadeAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade
     */
    void loadUser( long userId, AsyncCallback<org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade
     */
    void loadUsers( AsyncCallback<java.util.List<org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade
     */
    void saveUser( org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO userVO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade
     */
    void loadUsers( org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter config, AsyncCallback<org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult<org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade
     */
    void deleteUser( org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO userVO, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static UserServiceFacadeAsync instance;

        public static final UserServiceFacadeAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (UserServiceFacadeAsync) GWT.create( UserServiceFacade.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
