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

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.EventHandler;
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
import org.appverse.web.framework.frontend.gwt.rpc.ApplicationAsyncCallback;

import java.util.List;

@EventHandler
public class AuthenticationRpcCommandImpl extends
		AbstractRpcCommand<FrameworkEventBus> implements AuthenticationCommand {

	private final GWTAuthenticationServiceFacadeAsync service = GWT
			.create(GWTAuthenticationServiceFacade.class);

	private final ApplicationAsyncCallback<String> authenticationCallback = new ApplicationAsyncCallback<String>() {
		@Override
		public void onSuccess(String result) {
			XsrfRpcRequestBuilder.setXSRFToken(result);
			getService().getPrincipal(wrapCallback(principalCallback));
		}
	};

	private final ApplicationAsyncCallback<String> principalCallback = new ApplicationAsyncCallback<String>() {
		@Override
		public void onSuccess(String principal) {
			PrincipalInformation.setPrincipal(principal);
			getService().getAuthorities(wrapCallback(authoritiesCallback));

		}
	};

	@SuppressWarnings("rawtypes")
	private final ApplicationAsyncCallback<List> authoritiesCallback = new ApplicationAsyncCallback<List>() {
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
				wrapCallback((ApplicationAsyncCallback)callback));
	}

	@Override
	public void onGetXSRFSessionToken() {
		getService().getXSRFSessionToken(wrapCallback(authenticationCallback));
	}

	@Override
	public void onIsPrincipalAuthenticated(AppverseCallback<Boolean> callback) {
		getService().isPrincipalAuthenticated(wrapCallback((ApplicationAsyncCallback)callback));
	}

	@Override
	public void setAppEventBus(FrameworkEventBus eventBus) {
		setEventBus(eventBus);
	}
}
