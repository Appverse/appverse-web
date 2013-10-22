package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.impl.live;

import java.util.List;

import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.appverse.web.framework.frontend.gwt.json.ApplicationJsonAsyncCallback;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminEventBus;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.UserRestRpcCommand;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.gwt.commands.AbstractRestCommand;


public class UserRestRpcCommandImpl extends AbstractRestCommand<AdminEventBus> implements UserRestRpcCommand {

	static {
		org.fusesource.restygwt.client.Defaults.setDateFormat(null);
	}
	
	@Override
	public void deleteUser(UserVO user,
                           ApplicationJsonAsyncCallback<Void> asyncCallback) {
        UserServiceFacade.Client.get("deleteaUser").deleteUser(user, asyncCallback);
	}

	@Override
	public void loadUsers(ApplicationJsonAsyncCallback<List<UserVO>> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadUser(long userId, ApplicationJsonAsyncCallback<UserVO> callback) {
        UserServiceFacade.Client.get("loadUser").loadUser(Long.valueOf(userId), callback);
	}

	@Override
	public void loadUsers(
			GWTPresentationPaginatedDataFilter config,
			AppverseCallback<GWTPresentationPaginatedResult<UserVO>> callback) {
        UserServiceFacade.Client.get("loadUsers").loadUsers(config, callback);

	}

    @Override
    public void saveUser(UserVO user, ApplicationJsonAsyncCallback<Long> applicationRestAsyncCallback) {
        UserServiceFacade.Client.get("saveUser").saveUser(user, applicationRestAsyncCallback);
    }

}
