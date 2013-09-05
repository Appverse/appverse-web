package org.appverse.web.framework.frontend.gwt.callback;

import org.appverse.web.framework.frontend.gwt.rpc.ApplicationAsyncCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class AppverseCallback<T> extends ApplicationAsyncCallback<T> implements MethodCallback<T> {

	@Override
	public void onFailure(Method method, Throwable exception) {
		// TODO Session expired handling and default exception treatment needs to be implemented as for the RPC version
	}

	@Override
	public void onSuccess(Method method, T response) {
        // TODO Session expired handling and default exception treatment needs to be implemented as for the RPC version
	}
}
