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
package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.views.impl.gxt;

import java.util.ArrayList;

import javax.validation.ConstraintViolation;

import org.appverse.web.framework.frontend.gwt.views.AbstractEditorValidationView;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.editors.impl.gxt.UserVOEditor;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.presenters.interfaces.UserEditView;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.events.CancelEvent;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.events.DeleteEvent;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.events.SaveEvent;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.impl.gxt.ToolbarWidgetImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class UserEditViewImpl
		extends
		AbstractEditorValidationView<UserEditView.IUserEditPresenter, UserVO, UserVOEditor>
		implements UserEditView {

	interface Driver extends SimpleBeanEditorDriver<UserVO, UserVOEditor> {
	}

	interface UserEditViewUiBinder extends UiBinder<Widget, UserEditViewImpl> {
	}

	private static UserEditViewUiBinder uiBinder = GWT
			.create(UserEditViewUiBinder.class);
	@UiField
	UserVOEditor userEditor;

	@UiField
	ToolbarWidgetImpl userToolbar;

	@Override
	public void createDriver() {
		driver = GWT.create(Driver.class);
	}

	@Override
	public void createView() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(userEditor);
		userToolbar.addToolbarEventHandler(this);
	}

	/*
	 * Handler for delete event launched by ToolbarWidget
	 * 
	 * @see org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.handlers.
	 * SaveEventHandler
	 * #onSave(org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar
	 * .events.SaveEvent)
	 */
	@Override
	public void onCancel(final CancelEvent event) {
		presenter.cancel();
	}

	/*
	 * Handler for delete event launched by ToolbarWidget
	 * 
	 * @see org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.handlers.
	 * SaveEventHandler
	 * #onSave(org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar
	 * .events.SaveEvent)
	 */
	@Override
	public void onDelete(final DeleteEvent event) {
		final UserVO user = driver.flush();
		presenter.delete(user);
	}

	/*
	 * Handler for save event launched by ToolbarWidget
	 * 
	 * @see org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar.handlers.
	 * SaveEventHandler
	 * #onSave(org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.widgets.toolbar
	 * .events.SaveEvent)
	 */
	@Override
	public void onSave(final SaveEvent event) {
		final UserVO user = driver.flush();
		presenter.save(user);
	}

	@Override
	public void setCreationMode(final boolean canSave) {
		userToolbar.setCreationMode(canSave);
	}

	@Override
	public void setEditMode(final boolean canSave, final boolean canDelete) {
		userToolbar.setEditMode(canSave, canDelete);
	}

	@Override
	public void setUser(final UserVO user) {
		driver.edit(user);
		driver.setConstraintViolations(new ArrayList<ConstraintViolation<?>>());
	}
}