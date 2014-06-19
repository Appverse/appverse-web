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

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;

/**
 * This is the Async Callback provided by Appverse for RPC style communications.
 * It implements the @see com.google.gwt.user.client.rpc.AsyncCallback.
 * Main logic for onFailure method can be reused by Application, since it makes use of Appverse Notification Manager.
 * @param <T> Expected return type for onSuccess method.
 */
public class ApplicationAsyncCallback<T> extends AppverseCallback<T> implements AsyncCallback<T> {

	/**
	 * Default onFailure() method. If the exception is an instance of
	 * PresentationException and a code is informed we call
	 * handleApplicationExeption which provides a default treatment translating
	 * the exception code to a message and showing a dialog to the user without
	 * closing the browser tab. handleApplicationException can be overriden if
	 * we want to implement especific PresentationException treatment or just
	 * close the browser tab.
	 * If receive StatusCodeException, check expired session and call 
	 * handleExpiredSessionException(), which can be overridden
	 * 
	 * @param ex
	 *            Exception to handle.
	 */
	@Override
	public void onFailure(final Throwable ex) {
        super.onFailure(ex);
	}

	@Override
	public void onSuccess(final T paramT) { 
	}
}