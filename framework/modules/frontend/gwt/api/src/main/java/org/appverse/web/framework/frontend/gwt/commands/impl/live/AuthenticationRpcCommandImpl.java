package org.appverse.web.framework.frontend.gwt.commands.impl.live;

import java.util.List;

import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.model.presentation.UserInfoVO;
import org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTAuthenticationServiceFacade;
import org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTAuthenticationServiceFacadeAsync;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.appverse.web.framework.frontend.gwt.commands.AbstractRpcCommand;
import org.appverse.web.framework.frontend.gwt.commands.AuthenticationCommand;
import org.appverse.web.framework.frontend.gwt.common.FrameworkEventBus;
import org.appverse.web.framework.frontend.gwt.helpers.security.PrincipalInformation;
import org.appverse.web.framework.frontend.gwt.helpers.security.XsrfRpcRequestBuilder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mvp4g.client.annotation.EventHandler;

@EventHandler
public class AuthenticationRpcCommandImpl extends
		AbstractRpcCommand<FrameworkEventBus> implements AuthenticationCommand {

	private final GWTAuthenticationServiceFacadeAsync service = GWT
			.create(GWTAuthenticationServiceFacade.class);

	private final AppverseCallback<String> authenticationCallback = new AppverseCallback<String>() {
		@Override
		public void onSuccess(String result) {
			XsrfRpcRequestBuilder.setXSRFToken(result);
			getService().getPrincipal(wrapCallback(principalCallback));
		}
	};

	private final AppverseCallback<String> principalCallback = new AppverseCallback<String>() {
		@Override
		public void onSuccess(String principal) {
			PrincipalInformation.setPrincipal(principal);
			getService().getAuthorities(wrapCallback(authoritiesCallback));

		}
	};

	@SuppressWarnings("rawtypes")
	private final AppverseCallback<List> authoritiesCallback = new AppverseCallback<List>() {
		@SuppressWarnings({ "unchecked" })
		@Override
		public void onSuccess(List authorities) {
			PrincipalInformation.setAuthorities(authorities);
			eventBus.start();

		}
	};

	protected GWTAuthenticationServiceFacadeAsync getService() {
		return super.getService(service);
	}

	@Override
	public void onAuthenticate() {
		// getService().getXSRFSessionToken(wrapCallback(authenticationCallback));
		getService().getXSRFSessionToken(wrapCallback(authenticationCallback));
	}

	@Override
	public void onAuthenticatePrincipal(UserInfoVO userInfo,
			AppverseCallback<AuthorizationDataVO> callback) {
		getService().authenticatePrincipal(userInfo.getUser(), userInfo.getPassword(),
				wrapCallback(callback));
	}

	@Override
	public void onGetXSRFSessionToken() {
		getService().getXSRFSessionToken(wrapCallback(authenticationCallback));
	}

	@Override
	public void onIsPrincipalAuthenticated(AppverseCallback<Boolean> callback) {
		getService().isPrincipalAuthenticated(wrapCallback(callback));
	}

	@Override
	public void setAppEventBus(FrameworkEventBus eventBus) {
		setEventBus(eventBus);
	}
}
