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
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.google.gwt.core.shared.GWT;

public class AuthenticationRestCommandImpl extends
		AbstractRestCommand<FrameworkEventBus, AuthenticationRestServiceFacade> implements AuthenticationCommand {

	@Override
	public AuthenticationRestServiceFacade createService() {
		return GWT.create(AuthenticationRestServiceFacade.class);
	}



	private final MethodCallback<String> authenticationCallback = new MethodCallback<String>() {
		@Override
		public void onFailure(Method method, Throwable exception) {

		}

		@Override
		public void onSuccess(Method method, String response) {
			// TODO XSRF token comes into response...
			getRestService("authenticationServiceFacade/getPrincipal").getPrincipal(
					wrapCallback(principalCallback));
		}
	};

	private final MethodCallback<String> principalCallback = new MethodCallback<String>() {
		@Override
		public void onFailure(Method method, Throwable exception) {

		}

		@Override
		public void onSuccess(Method method, String response) {
			PrincipalInformation.setPrincipal(response);
			getRestService("authenticationServiceFacade/getAuthorities")
					.getAuthorities(wrapCallback(authoritiesCallback));
		}
	};

	private final MethodCallback<List<String>> authoritiesCallback = new MethodCallback<List<String>>() {
		@Override
		public void onFailure(Method method, Throwable exception) {

		}

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
				"authenticationServiceFacade/getXSRFSessionToken")
				.getXSRFSessionToken(authenticationCallback);

	}

	@Override
	public void onAuthenticatePrincipal(UserInfoVO userInfo,
			AppverseCallback<AuthorizationDataVO> callback) {
		getRestService(
				"authenticationServiceFacade/authenticatePrincipal")
				.authenticatePrincipal(userInfo, wrapCallback(callback));
	}

	@Override
	public void onGetXSRFSessionToken() {
		getRestService(
				"authenticationServiceFacade/getXSRFSessionToken")
				.getXSRFSessionToken(wrapCallback(authenticationCallback));
	}

	@Override
	public void onIsPrincipalAuthenticated(AppverseCallback<Boolean> callback) {
		getRestService(
				"authenticationServiceFacade/isPrincipalAuthenticated")
				.isPrincipalAuthenticated(wrapCallback(callback));
	}

	@Override
	public void setAppEventBus(FrameworkEventBus eventBus) {
		setEventBus(eventBus);
	}



}
