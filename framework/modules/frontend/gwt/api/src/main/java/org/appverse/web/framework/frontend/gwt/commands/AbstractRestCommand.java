package org.appverse.web.framework.frontend.gwt.commands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.mvp4g.client.event.EventBusWithLookup;
import org.appverse.web.framework.frontend.gwt.helpers.dispatcher.AppverseDispatcher;
import org.appverse.web.framework.frontend.gwt.managers.NotificationManager;
import org.fusesource.restygwt.client.*;

import java.util.Date;

public abstract class AbstractRestCommand<E extends EventBusWithLookup, T extends RestService>
		extends AbstractCommand<E> {

    static {
        org.fusesource.restygwt.client.Defaults.setDateFormat(null);
    }
	private T  instance = null;

	public final T getRestService(String serviceName, String methodName) {
		if (instance == null) {
			instance = createService();
		}
		((RestServiceProxy) instance).setResource(new Resource(GWT
				.getModuleBaseURL() + "rest/jsonservices/"+serviceName+"/"+ methodName));
        ((RestServiceProxy) instance).setDispatcher(AppverseDispatcher.INSTANCE);
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
