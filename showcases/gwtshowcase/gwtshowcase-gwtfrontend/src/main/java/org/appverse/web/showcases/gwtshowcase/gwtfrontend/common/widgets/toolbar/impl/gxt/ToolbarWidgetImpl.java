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
package org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.impl.gxt;

import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.ToolbarWidget;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.events.CancelEvent;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.events.DeleteEvent;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.events.SaveEvent;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.handlers.CancelEventHandler;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.handlers.DeleteEventHandler;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.handlers.SaveEventHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

@Singleton
public class ToolbarWidgetImpl extends Composite implements ToolbarWidget {

	interface ToolbarWidgetImplUiBinder extends
			UiBinder<Widget, ToolbarWidgetImpl> {
	}

	private static ToolbarWidgetImplUiBinder uiBinder = GWT
			.create(ToolbarWidgetImplUiBinder.class);

	private HandlerManager handlerManager;

	@UiField
	TextButton cancelButton, saveButton, deleteButton;

	@UiField
	HBoxLayoutContainer buttonsContainer;

	public ToolbarWidgetImpl() {
		initWidget();
	}

	public HandlerRegistration addToolbarEventHandler(final EventHandler handler) {
		HandlerRegistration reg = null;
		if (handler instanceof DeleteEventHandler) {
			reg = handlerManager.addHandler(DeleteEvent.TYPE,
					(DeleteEventHandler) handler);
		}
		if (handler instanceof CancelEventHandler) {
			reg = handlerManager.addHandler(CancelEvent.TYPE,
					(CancelEventHandler) handler);
		}
		if (handler instanceof DeleteEventHandler) {
			reg = handlerManager.addHandler(SaveEvent.TYPE,
					(SaveEventHandler) handler);
		}
		return reg;
	}

	/**
	 * This method will only create the widgets that are marked as provided. It
	 * does not add the widget to the panel: this is still done by the UI
	 * binder.
	 */
	private void initProvidedWidgets() {
	}

	public void initWidget() {
		handlerManager = new HandlerManager(this);
		initProvidedWidgets();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("cancelButton")
	public void onCancelButtonClick(final SelectEvent event) {
		handlerManager.fireEvent(new CancelEvent(event));
	}

	@UiHandler("deleteButton")
	public void ondDeleteButtonClick(final SelectEvent event) {
		handlerManager.fireEvent(new DeleteEvent(event));
	}

	@UiHandler("saveButton")
	public void onSaveButtonClick(final SelectEvent event) {
		handlerManager.fireEvent(new SaveEvent(event));
	}

	@Override
	public void setCreationMode(final boolean canSave) {
		if (buttonsContainer.getWidgetIndex(deleteButton) != -1) {
			buttonsContainer.remove(deleteButton);
		}
		if (!canSave) {
			saveButton.disable();
		}
	}

	@Override
	public void setEditMode(final boolean canSave, final boolean canDelete) {
		if (buttonsContainer.getWidgetIndex(deleteButton) == -1) {
			buttonsContainer.insert(deleteButton, 2);
		}
		if (!canSave) {
			saveButton.disable();
		}
		if (!canDelete) {
			deleteButton.disable();
		}
	}
}