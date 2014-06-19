/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License 
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this 
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the AppVerse Public License v2.0 
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
package org.appverse.web.framework.frontend.gwt.commands;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.mvp4g.client.event.EventBusWithLookup;
import org.appverse.web.framework.frontend.gwt.helpers.security.XsrfRpcRequestBuilder;
import org.appverse.web.framework.frontend.gwt.managers.NotificationManager;

import java.util.Date;

public abstract class AbstractRpcCommand<E extends EventBusWithLookup> extends
		AbstractCommand<E> {

	private static XsrfRpcRequestBuilder xsrfRpcRequestBuilder = new XsrfRpcRequestBuilder();

	protected <Service> Service getService(Service service) {
		((ServiceDefTarget) service)
				.setRpcRequestBuilder(xsrfRpcRequestBuilder);
		return service;
	}

	protected <T> AsyncCallback<T> wrapCallback(final AsyncCallback<T> callback) {

		AsyncCallback<T> internalCallback = new AsyncCallback<T>() {

			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.showError("Error Code: B001 "
						+ "Error Message:"
						+ caught.getMessage()
						+ "Error Time:"
						+ DateTimeFormat.getFormat(
								"yyyy-MM-dd'T'HH:mm:ss.SSSZZZ").format(
								new Date()));
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(T result) {
				callback.onSuccess(result);
			}
		};

		return internalCallback;
	}

}
