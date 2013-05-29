package org.appverse.web.framework.frontend.gwt.commands;

import java.util.Date;

import org.appverse.web.framework.backend.api.services.presentation.AuthenticationRestServiceFacade;
import org.appverse.web.framework.frontend.gwt.managers.NotificationManager;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.mvp4g.client.event.EventBusWithLookup;

public abstract class AbstractRestCommand<E extends EventBusWithLookup, T extends RestService>
		extends AbstractCommand<E> {

	private T  instance = null;

	public final T getRestService(String methodName) {
		if (instance == null) {
			instance = createService();
		}
		((RestServiceProxy) instance).setResource(new Resource(GWT
				.getModuleBaseURL() + "rest/jsonservices/" + methodName));
		return instance;

	}

	public abstract T createService();

	@SuppressWarnings("hiding")
	protected <T> MethodCallback<T> wrapCallback(
			final MethodCallback<T> callback) {
		MethodCallback<T> internalCallback = new MethodCallback<T>() {

			@Override
			public void onFailure(Method method, Throwable exception) {
				NotificationManager.showError("Error Code: B001 "
						+ "Error Message:"
						+ exception.getMessage()
						+ "Error Time:"
						+ DateTimeFormat.getFormat(
								"yyyy-MM-dd'T'HH:mm:ss.SSSZZZ").format(
								new Date()));
				callback.onFailure(method, exception);
			}

			@Override
			public void onSuccess(Method method, T response) {
				callback.onSuccess(method, response);
			}

		};
		return internalCallback;
	}

}
