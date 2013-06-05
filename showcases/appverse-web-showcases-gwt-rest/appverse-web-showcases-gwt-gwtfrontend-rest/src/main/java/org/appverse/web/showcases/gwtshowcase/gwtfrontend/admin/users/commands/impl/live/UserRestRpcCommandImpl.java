package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.impl.live;

import java.util.List;

import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminEventBus;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.UserRestRpcCommand;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.frontend.gwt.rest.ApplicationRestAsyncCallback;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.gwt.commands.AbstractRestCommand;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;


public class UserRestRpcCommandImpl extends AbstractRestCommand<AdminEventBus> implements UserRestRpcCommand {

	static {
		org.fusesource.restygwt.client.Defaults.setDateFormat(null);
	}
	
	@Override
	public void deleteUser(UserVO user,
			ApplicationRestAsyncCallback<Void> asyncCallback) {
		UserRestTestServiceFacade.Util.get("borradeleteUser").deleteUser(user, asyncCallback);
	}

	@Override
	public void loadUsers(ApplicationRestAsyncCallback<List<UserVO>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadUser(long userId, ApplicationRestAsyncCallback<UserVO> callback) {
		UserRestTestServiceFacade.Util.get("loadUser").loadUser(Long.valueOf(userId), callback);
	}

	@Override
	public void loadUsers(
			GWTPresentationPaginatedDataFilter config,
			ApplicationRestAsyncCallback<GWTPresentationPaginatedResult<UserVO>> callback) {
		UserRestTestServiceFacade.Util.get("loadUsers").loadUsers(config, callback);

	}

}
