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
package org.appverse.web.framework.frontend.gwt.rpc;

import java.util.Date;

import org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTPresentationException;
import org.appverse.web.framework.frontend.gwt.managers.NotificationManager;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ApplicationAsyncCallback<T> implements AsyncCallback<T> {

	// TODO: See how to do this so that we can have a ExceptionTranslator (with
	// property files in the application) but
	// supported by the framework. See for instance if we can use
	// "GWT Injection" / Deferred binding using an interface class
	// an use a "replace-with" to change the implementation (which has to be in
	// the application)
	/**
	 * Default Presentation Exception treatment closing the browser tab. If the
	 * exception code is informed and is coded in ExceptionMessages.properties
	 * it will be automatically translated and the corresponding message will be
	 * shown to the user. If the exception code is not informed or is not coded
	 * in ExceptionMessages.properties a generic exception notification will be
	 * shown to the user using the exception message information.
	 * 
	 * @param PresentationException
	 *            PresentationException instance to handle
	 */
	// public void defaultPresentationExceptionTreatmentClosingWindow(
	// PresentationException ex) {
	// String translatedMessage = ExceptionTranslationManager.translate(ex);
	// ApplicationNotificationManager am = new ApplicationNotificationManager();
	// am.showExceptionAndClose(ex, translatedMessage);
	// }

	// TODO: See how to do this so that we can have a ExceptionTranslator (with
	// property files in the application) but
	// supported by the framework. See for instance if we can use
	// "GWT Injection" / Deferred binding using an interface class
	// an use a "replace-with" to change the implementation (which has to be in
	// the application)
	/**
	 * Default Presentation Exception treatment without closing the browser tab.
	 * If the exception code is informed and is coded in
	 * ExceptionMessages.properties it will be automatically translated and the
	 * corresponding message will be shown to the user. If the exception code is
	 * not informed or is not coded in ExceptionMessages.properties a generic
	 * exception notification will be shown to the user using the exception
	 * message information.
	 * 
	 * @param PresentationException
	 *            PresentationException instance to handle
	 */
	// public void defaultPresentationExceptionTreatmentWithoutClosingWindow(
	// final PresentationException ex) {
	// String translatedMessage = ExceptionTranslationManager.translate(ex);
	// ApplicationNotificationManager am = new ApplicationNotificationManager();
	// am.showException(ex, translatedMessage);
	// }

	public void defaultPresentationExceptionTreatmentWithoutClosingWindow(
			final GWTPresentationException ex) {
		NotificationManager.showError("Error Message:"
				+ ex.getMessage()
				+ "Error Time:"
				+ DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ")
						.format(new Date()));
	}

	/**
	 * Default Presentation Exception treatment. Default treatment will not
	 * close the browser tab This method can be overriden (without calling
	 * super()) to implement a particular treatment
	 * 
	 * @param PresentationException
	 *            PresentationException instance to handle.
	 */
	// public void handlePresentationException(final PresentationException ex) {
	// defaultPresentationExceptionTreatmentWithoutClosingWindow(ex);
	// }

	public void handlePresentationException(final GWTPresentationException ex) {
		defaultPresentationExceptionTreatmentWithoutClosingWindow(ex);
	}

	/**
	 * Default onFailure() method. If the exception is an instance of
	 * PresentationException and a code is informed we call
	 * handleApplicationExeption which provides a default treatment translating
	 * the exception code to a message and showing a dialog to the user without
	 * closing the browser tab. handleApplicationException can be overriden if
	 * we want to implement especific PresentationException treatment or just
	 * close the broser tab.
	 * 
	 * @param Throwable
	 *            Exception to handle.
	 */
	@Override
	public void onFailure(final Throwable ex) {
		GWTPresentationException pex = null;
		if (ex instanceof GWTPresentationException) {
			pex = (GWTPresentationException) ex;
			// Application exception (with an application code)
		} else {
			// We should not receive exceptions different from
			// GWTPresentationException in RPC commands. We control this just
			// for security.
			pex = new GWTPresentationException(ex.getMessage(), null);
		}
		handlePresentationException(pex);
		return;
	}

	@Override
	public void onSuccess(final T paramT) {
	}
}
