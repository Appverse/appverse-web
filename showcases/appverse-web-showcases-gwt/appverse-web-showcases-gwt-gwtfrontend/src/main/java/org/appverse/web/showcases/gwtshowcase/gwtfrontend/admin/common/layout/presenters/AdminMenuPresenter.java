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
package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.layout.presenters;

import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminEventBus;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.layout.presenters.interfaces.AdminMenuView;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.layout.views.impl.gxt.AdminMenuViewImpl;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

@Presenter(view = AdminMenuViewImpl.class)
public class AdminMenuPresenter extends
		LazyPresenter<AdminMenuView, AdminEventBus> implements
		AdminMenuView.IAdminMenuPresenter {

	private void applySecurity() {
		// Example, if you wanted to hide menu options depending on user authorities, you can do it here:
		//		if (!PrincipalInformation.hasPrincialAuthority("ROLE_USER_VIEW")) {
		//				view.hideEnterpriseUsers();
		//		}
	}

	@Override
	public void bindView() {
		applySecurity();
	}

	@Override
	public void enterpriseUsersClicked() {
		eventBus.usersSearch(false);
	}
	
	@Override
	public void homeClicked() {
		eventBus.homeClicked();
	}	

	@Override
	public void logoutClicked() {
		eventBus.logout();
	}
}