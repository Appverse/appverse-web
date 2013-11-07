package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.impl.live;

import java.util.List;

import com.google.gwt.core.client.GWT;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.appverse.web.framework.frontend.gwt.commands.AbstractRestCommand;
import org.appverse.web.framework.frontend.gwt.json.ApplicationJsonAsyncCallback;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.backend.services.presentation.UserServiceFacade;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminEventBus;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.commands.UserRestRpcCommand;
import org.fusesource.restygwt.client.RestService;


public class UserRestRpcCommandImpl extends AbstractRestCommand<AdminEventBus,UserServiceFacade.UserRestServiceFacade> implements UserRestRpcCommand {

	
	@Override
	public void deleteUser(UserVO user,
                           ApplicationJsonAsyncCallback<Void> asyncCallback) {
        getRestService("userRestServiceFacade","deleteUser").deleteUser(user, asyncCallback);
	}

	@Override
	public void loadUser(long userId, ApplicationJsonAsyncCallback<UserVO> callback) {
        getRestService("userRestServiceFacade","loadUser").loadUser(Long.valueOf(userId), callback);
	}

	@Override
	public void loadUsers(
			GWTPresentationPaginatedDataFilter config,
			AppverseCallback<GWTPresentationPaginatedResult<UserVO>> callback) {
        getRestService("userRestServiceFacade","loadUsers").loadUsers(config, callback);
	}

    @Override
    public void saveUser(UserVO user, ApplicationJsonAsyncCallback<Long> applicationRestAsyncCallback) {
        getRestService("userRestServiceFacade","saveUser").saveUser(user, applicationRestAsyncCallback);
    }

    @Override
    public UserServiceFacade.UserRestServiceFacade createService() {
        return GWT.create(UserServiceFacade.UserRestServiceFacade.class);
    }
}
