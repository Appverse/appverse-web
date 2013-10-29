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
package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.injection;

import org.appverse.web.framework.frontend.gwt.commands.AuthenticationCommand;
import org.appverse.web.framework.frontend.gwt.commands.impl.live.AuthenticationRestCommandImpl;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminConstants;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminImages;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminMessages;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.UserRestRpcCommand;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.impl.live.UserRestRpcCommandImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class AdminGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AdminConstants.class).in(Singleton.class);
		bind(AdminMessages.class).in(Singleton.class);
		bind(AdminImages.class).in(Singleton.class);
		bind(AuthenticationCommand.class).to(
				AuthenticationRestCommandImpl.class);
//		bind(AuthenticationCommand.class).to(
//		AuthenticationCommandRpcImpl.class);
		bind(UserRestRpcCommand.class).to(UserRestRpcCommandImpl.class);
	}

}
