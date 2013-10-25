/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Mozilla Public 
 License, v. 2.0. If a copy of the MPL was not distributed with this 
 file, You can obtain one at http://mozilla.org/MPL/2.0/. 

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the Mozilla Public License v2.0 
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
package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.presenters;

import org.appverse.web.framework.frontend.gwt.helpers.security.PrincipalInformation;
import org.appverse.web.framework.frontend.gwt.json.ApplicationJsonAsyncCallback;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminEventBus;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminMessages;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.injection.AdminInjector;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.UserRestRpcCommand;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.presenters.interfaces.UserEditView;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.views.impl.gxt.UserEditViewImpl;
import org.appverse.web.showcases.gwtshowcase.backend.constants.AuthoritiesConstants;
import org.fusesource.restygwt.client.Method;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

@Presenter(view = UserEditViewImpl.class)
public class UserEditPresenter extends
		LazyPresenter<UserEditView, AdminEventBus> implements
		UserEditView.IUserEditPresenter {

	private AdminInjector injector;
	private AdminMessages adminMessages;
	private UserRestRpcCommand userRestRpcCommand;
	
	@Override
	public void bindView() {
	}

	// Button cancel pressed
	@Override
	public void cancel() {
		eventBus.usersSearch(false);
	}

	@Override
	public void createPresenter() {
		injector = AdminInjector.INSTANCE;
		adminMessages = injector.getAdminMessages();
//		userRpcCommand = injector.getUserRpcCommand();
		userRestRpcCommand = injector.getUserRestRpcCommand();
	}

	// Button delete pressed
	@Override
	public void delete(final UserVO user) {

		ConfirmMessageBox box = new ConfirmMessageBox(adminMessages.confirmation(),
				adminMessages.confirmDeletion());

		box.addHideHandler(new HideHandler() {
			@Override
			public void onHide(final HideEvent event) {
				Dialog btn = (Dialog) event.getSource();
				String answer = btn.getHideButton().getText();
				if (btn.getDialogMessages().yes().equals(answer)) {
					userRestRpcCommand.deleteUser(user,
							new ApplicationJsonAsyncCallback<Void>() {
                                @Override
                                public void onSuccess(Method method, Void o) {
                                    eventBus.usersSearch(true);
                                }
							});
				}
			}
		});
		box.show();

	}

	private void loadMappings() {
		// If you had reference data you can load it here
	}

	@Override
	public void onUserEdit(final UserVO user) {
        view.setUser(user);
        if (user.getId() == 0) {
            view.setCreationMode(PrincipalInformation
                    .hasPrincialAuthority(AuthoritiesConstants.ROLE_USER_CREATE));
        } else {
            view.setEditMode(
                    PrincipalInformation
                            .hasPrincialAuthority(AuthoritiesConstants.ROLE_USER_EDIT),
                    PrincipalInformation
                            .hasPrincialAuthority(AuthoritiesConstants.ROLE_USER_DISABLE));
        }
        loadMappings();
        eventBus.adminLayoutChangeBody(view.asWidget());
	}

	// Button save pressed
	@Override
	public void save(final UserVO user) {
		boolean valid = view.validate(user);
		if (valid) {
			userRestRpcCommand.saveUser(user, new ApplicationJsonAsyncCallback<Long>() {
                @Override
                public void onSuccess(Method method, Long o) {
                    eventBus.usersSearch(true);
                }
			});
		}
	}
}