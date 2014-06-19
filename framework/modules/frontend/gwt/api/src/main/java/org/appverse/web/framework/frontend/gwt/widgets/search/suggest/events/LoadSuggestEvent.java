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
package org.appverse.web.framework.frontend.gwt.widgets.search.suggest.events;

import com.google.gwt.event.shared.GwtEvent;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTAbstractPresentationBean;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.handlers.LoadSuggestEventHandler;


@SuppressWarnings("rawtypes")
public class LoadSuggestEvent<M extends GWTAbstractPresentationBean> extends
		GwtEvent<LoadSuggestEventHandler> {

	public static Type<LoadSuggestEventHandler> TYPE = new Type<LoadSuggestEventHandler>();

	private final GWTPresentationPaginatedDataFilter config;
	private final AppverseCallback<GWTPresentationPaginatedResult<M>> callback;

	public LoadSuggestEvent(GWTPresentationPaginatedDataFilter config,
                            AppverseCallback<GWTPresentationPaginatedResult<M>> callback) {
		this.config = config;
		this.callback = callback;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void dispatch(final LoadSuggestEventHandler handler) {
		handler.onLoad(this);
	}

	@Override
	public Type<LoadSuggestEventHandler> getAssociatedType() {
		return TYPE;
	}

	public AppverseCallback<GWTPresentationPaginatedResult<M>> getCallback() {
		return callback;
	}

	public GWTPresentationPaginatedDataFilter getConfig() {
		return config;
	}

}
