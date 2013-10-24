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
package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin;

import org.appverse.web.framework.frontend.gwt.commands.impl.live.LogoutManagementRpcCommandImpl;
import org.appverse.web.framework.frontend.gwt.common.FrameworkEventBus;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.commands.impl.live.InitializerRpcCommandImpl;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.layout.presenters.AdminHeaderPresenter;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.layout.presenters.AdminLayoutPresenter;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.layout.presenters.AdminMenuPresenter;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.layout.presenters.AdminModuleLayoutManagerPresenter;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.history.AdminHistoryConverter;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.home.presenters.HomePresenter;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.presenters.UserEditPresenter;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.presenters.UserSearchPresenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;

@Events(startPresenter = AdminModuleLayoutManagerPresenter.class, historyOnStart = false)
public interface AdminEventBus extends FrameworkEventBus {

	@Event(handlers = { AdminLayoutPresenter.class })
	public void adminLayoutChangeBody(Widget bodyWidget);

	@Event(handlers = { AdminModuleLayoutManagerPresenter.class })
	public void changeLayoutWidget(IsWidget layoutWidget);

	@Event(handlers = { HomePresenter.class })
	public void homeClicked();

	@Event(bind = { AdminMenuPresenter.class, AdminHeaderPresenter.class }, handlers = {
			AdminLayoutPresenter.class, /*HomePresenter.class*/ UserSearchPresenter.class })
	@InitHistory
	public void init();

	@Event(handlers = { LogoutManagementRpcCommandImpl.class })
	public void logout();

	@Override
	@Event(handlers = { InitializerRpcCommandImpl.class })
	public void start();

	@Event(handlers = { UserEditPresenter.class })
	public void userEdit(UserVO user);
	
	@Event(handlers = { UserSearchPresenter.class })
	public void usersSearch(boolean refresh);
	
	@Event(handlers = { UserSearchPresenter.class }, historyConverter = AdminHistoryConverter.class, name = "placeUserSearch", navigationEvent = true)
	public void placeUserSearch();		
}