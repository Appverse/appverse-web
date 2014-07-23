package org.appverse.web.framework.frontend.gwt.commands;

import java.util.Date;

import org.appverse.web.framework.frontend.gwt.managers.NotificationManager;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.mvp4g.client.event.EventBusWithLookup;

public abstract class AbstractRestJSONCommand<E extends EventBusWithLookup>
		extends AbstractCommand<E> {


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
