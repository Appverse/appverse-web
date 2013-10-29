package org.appverse.web.framework.frontend.gwt.commands.impl.live;

import java.util.List;

import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.model.presentation.UserInfoVO;
import org.appverse.web.framework.backend.api.services.presentation.AuthenticationRestServiceFacade;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.appverse.web.framework.frontend.gwt.commands.AbstractRestCommand;
import org.appverse.web.framework.frontend.gwt.commands.AuthenticationCommand;
import org.appverse.web.framework.frontend.gwt.common.FrameworkEventBus;
import org.appverse.web.framework.frontend.gwt.helpers.security.PrincipalInformation;
import org.appverse.web.framework.frontend.gwt.json.ApplicationJsonAsyncCallback;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.shared.GWT;

public class AuthenticationRestCommandImpl extends
		AbstractRestCommand<FrameworkEventBus, AuthenticationRestServiceFacade> implements AuthenticationCommand {
    static final String SERVICE_NAME = "authenticationServiceFacade";
	@Override
	public AuthenticationRestServiceFacade createService() {
		return GWT.create(AuthenticationRestServiceFacade.class);
	}



	private final ApplicationJsonAsyncCallback<String> authenticationCallback = new ApplicationJsonAsyncCallback<String>() {
		@Override
		public void onSuccess(Method method, String response) {
			// TODO XSRF token comes into response...
			getRestService(SERVICE_NAME,"getPrincipal").getPrincipal(
					wrapCallback(principalCallback));
		}
	};

	private final ApplicationJsonAsyncCallback<String> principalCallback = new ApplicationJsonAsyncCallback<String>() {
		@Override
		public void onSuccess(Method method, String response) {
			PrincipalInformation.setPrincipal(response);
			getRestService(SERVICE_NAME,"getAuthorities")
					.getAuthorities(wrapCallback(authoritiesCallback));
		}
	};

	private final ApplicationJsonAsyncCallback<List<String>> authoritiesCallback = new ApplicationJsonAsyncCallback<List<String>>() {
		@Override
		public void onSuccess(Method method, List<String> response) {
			PrincipalInformation.setAuthorities(response);
			eventBus.start();
		}
	};

	@Override
	public void onAuthenticate() {
		// TODO Auto-generated method stub
		getRestService(
                SERVICE_NAME,"getXSRFSessionToken")
				.getXSRFSessionToken(authenticationCallback);

	}

	@Override
	public void onAuthenticatePrincipal(UserInfoVO userInfo,
			AppverseCallback<AuthorizationDataVO> callback) {
		getRestService(
                SERVICE_NAME,"authenticatePrincipal")
				.authenticatePrincipal(userInfo, wrapCallback((ApplicationJsonAsyncCallback) callback));
	}

	@Override
	public void onGetXSRFSessionToken() {
		getRestService(
                SERVICE_NAME,"getXSRFSessionToken")
				.getXSRFSessionToken(wrapCallback(authenticationCallback));
	}

	@Override
	public void onIsPrincipalAuthenticated(AppverseCallback<Boolean> callback) {
		getRestService(
                SERVICE_NAME,"isPrincipalAuthenticated")
				.isPrincipalAuthenticated(wrapCallback((ApplicationJsonAsyncCallback) callback));
	}

	@Override
	public void setAppEventBus(FrameworkEventBus eventBus) {
		setEventBus(eventBus);
	}



}
