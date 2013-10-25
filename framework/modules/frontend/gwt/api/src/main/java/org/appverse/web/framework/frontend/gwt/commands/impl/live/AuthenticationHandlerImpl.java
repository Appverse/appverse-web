package org.appverse.web.framework.frontend.gwt.commands.impl.live;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.model.presentation.UserInfoVO;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.appverse.web.framework.frontend.gwt.commands.AbstractRpcCommand;
import org.appverse.web.framework.frontend.gwt.commands.AuthenticationCommand;
import org.appverse.web.framework.frontend.gwt.commands.AuthenticationHandler;
import org.appverse.web.framework.frontend.gwt.common.FrameworkEventBus;

@EventHandler
public class AuthenticationHandlerImpl extends
		AbstractRpcCommand<FrameworkEventBus> implements AuthenticationHandler {

	private AuthenticationCommand authCommand; // injected

	@Inject
	public AuthenticationHandlerImpl(AuthenticationCommand authCommand) {
		this.authCommand = authCommand;
	}

	/**
	 * This is the first event managed by Appverse Framework (annotated with @
	 * Start in FrameworkEventBus). As this Implementation is going to dispatch
	 * to a concrete implementation (Default RPC) based on Gin Module of the
	 * application, we need to set the EventBus to this implementation via the
	 * AuthCommand interface.
	 * 
	 */
	@Override
	public void onAuthenticate() {
		authCommand.setAppEventBus(eventBus);
		authCommand.onAuthenticate();
	}

	@Override
	public void onAuthenticatePrincipal(UserInfoVO userInfo,
                                        AppverseCallback<AuthorizationDataVO> callback) {
		authCommand.onAuthenticatePrincipal(userInfo, callback);
	}

	@Override
	public void onGetXSRFSessionToken() {
		authCommand.onGetXSRFSessionToken();
	}

	@Override
	public void onIsPrincipalAuthenticated(AppverseCallback<Boolean> callback) {
		authCommand.onIsPrincipalAuthenticated(callback);
	}
}
