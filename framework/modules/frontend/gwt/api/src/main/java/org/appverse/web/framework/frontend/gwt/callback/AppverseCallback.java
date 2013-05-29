package org.appverse.web.framework.frontend.gwt.callback;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppverseCallback<T> implements AsyncCallback<T>, MethodCallback<T> {

	@Override
	public void onFailure(Method method, Throwable exception) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(Method method, T response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(T result) {
		// TODO Auto-generated method stub
		
	}

}
