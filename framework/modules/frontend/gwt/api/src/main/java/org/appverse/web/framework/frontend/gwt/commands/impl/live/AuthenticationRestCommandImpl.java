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
package org.appverse.web.framework.frontend.gwt.commands.impl.live;

import java.util.List;

import javax.inject.Inject;

import org.appverse.web.framework.backend.api.model.presentation.AuthorizationDataVO;
import org.appverse.web.framework.backend.api.model.presentation.UserInfoVO;
import org.appverse.web.framework.backend.api.services.presentation.AuthenticationRestServiceFacade;
import org.appverse.web.framework.frontend.gwt.helpers.dispatcher.AppverseDispatcher;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.appverse.web.framework.frontend.gwt.commands.AbstractRestCommand;
import org.appverse.web.framework.frontend.gwt.commands.AuthenticationCommand;
import org.appverse.web.framework.frontend.gwt.common.FrameworkEventBus;
import org.appverse.web.framework.frontend.gwt.helpers.security.PrincipalInformation;
import org.appverse.web.framework.frontend.gwt.json.ApplicationJsonAsyncCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.TextCallback;

import com.google.gwt.core.client.GWT;

public class AuthenticationRestCommandImpl extends
		AbstractRestCommand<FrameworkEventBus, AuthenticationRestServiceFacade> implements AuthenticationCommand {

	@Inject
	AuthenticationRestServiceFacade authServiceFacade;
	
	static final String SERVICE_NAME = "authenticationServiceFacade";
	@Override
	public AuthenticationRestServiceFacade createService() {
		if( true ) 
			throw new RuntimeException("createService in AuthenticationRestCommandImpl used!");
		return GWT.create(AuthenticationRestServiceFacade.class);
	}



	//private final ApplicationJsonAsyncCallback<String> authenticationCallback = new ApplicationJsonAsyncCallback<String>() {
	private final TextCallback authenticationCallback = new TextCallback() {
		@Override
		public void onSuccess(Method method, String response) {
			// TODO XSRF token comes into response...
            AppverseDispatcher.setXSRFToken(response);
            /*authServiceFacade.getPrincipal(
					wrapCallback(principalCallback));*/
            authServiceFacade.getPrincipal(principalCallback);
			/*getRestService(SERVICE_NAME,"getPrincipal").getPrincipal(
					wrapCallback(principalCallback));*/
		}

		@Override
		public void onFailure(Method method, Throwable exception) {
			// TODO Auto-generated method stub
			
		}
	};

	//private final ApplicationJsonAsyncCallback<String> principalCallback = new ApplicationJsonAsyncCallback<String>() {
	private final TextCallback principalCallback = new TextCallback() {
		@Override
		public void onSuccess(Method method, String response) {
			PrincipalInformation.setPrincipal(response);
			authServiceFacade.getAuthorities(wrapCallback(authoritiesCallback));
			/*getRestService(SERVICE_NAME,"getAuthorities")
					.getAuthorities(wrapCallback(authoritiesCallback));*/
		}
		@Override
		public void onFailure(Method method, Throwable exception) {
			// TODO Auto-generated method stub
			
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
		authServiceFacade.getXSRFSessionToken(authenticationCallback);
		/*getRestService(
                SERVICE_NAME,"getXSRFSessionToken")
				.getXSRFSessionToken(authenticationCallback);*/
	}

	@Override
	public void onAuthenticatePrincipal(UserInfoVO userInfo,
			AppverseCallback<AuthorizationDataVO> callback) {
		authServiceFacade.authenticatePrincipal(
				userInfo, 
				wrapCallback((ApplicationJsonAsyncCallback) callback));
		/*getRestService(
                SERVICE_NAME,"authenticatePrincipal")
				.authenticatePrincipal(userInfo, wrapCallback((ApplicationJsonAsyncCallback) callback));*/
	}

	@Override
	public void onGetXSRFSessionToken() {
		authServiceFacade
		.getXSRFSessionToken(authenticationCallback);
		/*authServiceFacade
			.getXSRFSessionToken(wrapCallback(authenticationCallback));*/
		/*getRestService(
                SERVICE_NAME,"getXSRFSessionToken")
				.getXSRFSessionToken(wrapCallback(authenticationCallback));*/
	}

	@Override
	public void onIsPrincipalAuthenticated(AppverseCallback<Boolean> callback) {
		authServiceFacade
				.isPrincipalAuthenticated(wrapCallback((ApplicationJsonAsyncCallback) callback));
		/*getRestService(
                SERVICE_NAME,"isPrincipalAuthenticated")
				.isPrincipalAuthenticated(wrapCallback((ApplicationJsonAsyncCallback) callback));*/
	}

	@Override
	public void setAppEventBus(FrameworkEventBus eventBus) {
		setEventBus(eventBus);
	}

}
