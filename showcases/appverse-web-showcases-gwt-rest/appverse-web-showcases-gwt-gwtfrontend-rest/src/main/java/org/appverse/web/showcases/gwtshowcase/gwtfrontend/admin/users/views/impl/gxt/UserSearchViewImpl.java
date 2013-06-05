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
import java.util.List;

import org.appverse.web.framework.backend.api.model.presentation.PresentationDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult;
import org.appverse.web.framework.frontend.gwt.helpers.filters.GxtPaginationConverter;
import org.appverse.web.framework.frontend.gwt.rmvp.ReverseComposite;
import org.appverse.web.framework.frontend.gwt.rpc.ApplicationAsyncCallback;
import org.appverse.web.framework.frontend.gwt.theme.client.search.AppverseSuggestAppearance.RiaSuggestStyle;
import org.appverse.web.framework.frontend.gwt.theme.client.search.SuggestTemplate;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.events.LoadSuggestEvent;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.events.SelectSuggestEvent;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.handlers.LoadSuggestEventHandler;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.handlers.SelectSuggestEventHandler;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.impl.gxt.SuggestWidgetImpl;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.model.SuggestModel;
import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.AdminMessages;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.common.injection.AdminInjector;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.presenters.interfaces.UserSearchView;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.frontend.gwt.rest.ApplicationRestAsyncCallback;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.HttpProxy;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

@Singleton
public class UserSearchViewImpl extends
		ReverseComposite<UserSearchView.IUserSearchPresenter> implements
		UserSearchView {

	interface Binder extends UiBinder<Widget, UserSearchViewImpl> {
	}

	// Template to display VO in suggest cell
	interface MySuggestTemplate extends SuggestTemplate<UserVO> {
		@Override
		@XTemplate("<div class='{style.searchItem}'><h3><span>{user.created:date(\"d/M/yyyy\")}<br/>{user.createdBy}</span>{user.name}</h3></div>")
		SafeHtml render(UserVO user, RiaSuggestStyle style);
	}

	public interface UserVOProperties extends PropertyAccess<UserVO> {

		ValueProvider<UserVO, Boolean> active();

		ValueProvider<UserVO, String> email();

		ModelKeyProvider<UserVO> id();

		@Path("id")
		ValueProvider<UserVO, Long> idProp();

		ValueProvider<UserVO, String> lastName();

		ValueProvider<UserVO, String> name();

		ValueProvider<UserVO, String> signup();

	}

	// Marker properties for SuggestField
	public interface UserVOSugProperties extends SuggestModel<UserVO> {
		@Override
		@Path("description")
		ValueProvider<UserVO, String> description();

		@Override
		@Path("id")
		ModelKeyProvider<UserVO> id();

		@Override
		@Path("name")
		LabelProvider<UserVO> label();

		@Override
		@Path("name")
		ValueProvider<UserVO, String> name();
	}

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField(provided = true)
	ListStore<UserVO> store;

	@UiField(provided = true)
	ColumnModel<UserVO> cm;

	@UiField
	GridView<UserVO> view;

	@UiField
	Grid<UserVO> userListTable;

	@UiField
	PagingToolBar toolBar;

	@UiField
	TextButton addUserButton, searchUsersButton;

	@UiField
	SuggestWidgetImpl<UserVO> suggestSearch;

	ApplicationAsyncCallback<GWTPresentationPaginatedResult<UserVO>> callbackListUsers;
	
	ApplicationRestAsyncCallback<GWTPresentationPaginatedResult<UserVO>> callbackRestListUsers;
	
	GWTPresentationPaginatedDataFilter dataFilter;

	boolean disableEditFeature = false;

	@Override
	public Widget asWidget() {
		return this;
	}

	@UiFactory
	ColumnModel<UserVO> createColumnModel() {
		return cm;
	}

	@UiFactory
	ListStore<UserVO> createListStore() {
		return store;
	}

	@Override
	public void createView() {
		initProvidedWidgets();
		initWidget(uiBinder.createAndBindUi(this));
		initLoader();
		// Selection list option clicked
		suggestSearch.addSuggestEventHandler(new SelectSuggestEventHandler() {
			@Override
			public void onSelect(final SelectSuggestEvent event) {
				presenter.searchUsers();
			}
		});
		suggestSearch
				.addSuggestEventHandler(new LoadSuggestEventHandler<UserVO>() {
					// Load list
					@Override
					public void onLoad(final LoadSuggestEvent<UserVO> event) {
						presenter.loadUsers(event.getConfig(),
								event.getCallback());
					}
				});
	}

	@Override
	public void disableAddFeature() {
		addUserButton.disable();
	}

	@Override
	public void disableEditFeature() {
		disableEditFeature = true;
	}

	@Override
	public ApplicationAsyncCallback<GWTPresentationPaginatedResult<UserVO>> getCallbackListUsers() {
		return callbackListUsers;
	}

	@Override
	public ApplicationRestAsyncCallback<GWTPresentationPaginatedResult<UserVO>> getCallbackRestListUsers() {
		return callbackRestListUsers;
	}

	@Override
	public GWTPresentationPaginatedDataFilter getDataFilter() {
		// TODO search automatic filter update system
		// dataFilter.getColumns().clear();
		// dataFilter.getValues().clear();
		dataFilter.resetConditions();

		if ((suggestSearch.getText().trim() != null)
				&& (suggestSearch.getText().trim().length() > 0)) {
			StringBuilder sb = new StringBuilder();
			sb.append(PresentationDataFilter.WILDCARD_ALL)
					.append(suggestSearch.getText())
					.append(PresentationDataFilter.WILDCARD_ALL);
			// dataFilter.getValues().add(sb.toString());
			// dataFilter.getColumns().add("name");
			// dataFilter.getLikes().add(true);
			dataFilter.addLikeCondition("name", sb.toString());
		}
		return dataFilter;
	}

	public void initLoader() {
		final RestyGWTProxy<PagingLoadConfig, PagingLoadResult<UserVO>> proxyResty = new RestyGWTProxy<PagingLoadConfig, PagingLoadResult<UserVO>>() {

			@Override
			public void load(
					final PagingLoadConfig loadConfig,
					final ApplicationRestAsyncCallback<PagingLoadResult<UserVO>> callback) {
				callbackRestListUsers = new ApplicationRestAsyncCallback<GWTPresentationPaginatedResult<UserVO>>() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(
							GWTPresentationPaginatedResult<UserVO> result) {
						callback.onSuccess((PagingLoadResult<UserVO>) GxtPaginationConverter
								.convert(result));
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Method method,
							GWTPresentationPaginatedResult<UserVO> result) {
						callback.onSuccess((PagingLoadResult<UserVO>) GxtPaginationConverter
								.convert(result));
					}
					
				};
				dataFilter = GxtPaginationConverter.convert(loadConfig);
				presenter.loadUsers(getDataFilter(), callbackRestListUsers);
			}

			
		};
		/*final RpcProxy<PagingLoadConfig, PagingLoadResult<UserVO>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<UserVO>>() {
			@Override
			public void load(final PagingLoadConfig loadConfig,
					final AsyncCallback<PagingLoadResult<UserVO>> callback) {
				callbackListUsers = new ApplicationAsyncCallback<GWTPresentationPaginatedResult<UserVO>>() {
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(
							final GWTPresentationPaginatedResult<UserVO> result) {
						callback.onSuccess((PagingLoadResult<UserVO>) GxtPaginationConverter
								.convert(result));
					}
				};
				dataFilter = GxtPaginationConverter.convert(loadConfig);
				presenter.loadUsers(getDataFilter(), callbackListUsers);
			}
		};*/

		final PagingLoader<PagingLoadConfig, PagingLoadResult<UserVO>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<UserVO>>(
				proxyResty);
		loader.setRemoteSort(true);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, UserVO, PagingLoadResult<UserVO>>(
				store));

		userListTable.setLoader(loader);
		toolBar.bind(loader);
		loader.load();

		MySuggestTemplate template = GWT.create(MySuggestTemplate.class);
		// It's REQUIRED to set a template
		suggestSearch.setTemplate(template);
	}

	/**
	 * This method will only create the widgets that are marked as provided. It
	 * does not add the widget to the panel: this is still done by the UI
	 * binder.
	 */
	private void initProvidedWidgets() {
		final UserVOProperties props = GWT.create(UserVOProperties.class);
		store = new ListStore<UserVO>(new ModelKeyProvider<UserVO>() {
			@Override
			public String getKey(final UserVO item) {
				return "" + item.getId();
			}
		});
		final AdminMessages am = AdminInjector.INSTANCE.getAdminMessages();
		final ColumnConfig<UserVO, Long> idColumn = new ColumnConfig<UserVO, Long>(
				props.idProp(), 25, am.usermanagerTableId());
		final ColumnConfig<UserVO, String> nameColumn = new ColumnConfig<UserVO, String>(
				props.name(), 50, am.usermanagerTableName());
		final ColumnConfig<UserVO, String> lastNameColumn = new ColumnConfig<UserVO, String>(
				props.lastName(), 50, am.usermanagerTableLastName());
		final ColumnConfig<UserVO, String> emailColumn = new ColumnConfig<UserVO, String>(
				props.email(), 75, am.usermanagerTableEmail());
		final ColumnConfig<UserVO, String> signupColumn = new ColumnConfig<UserVO, String>(
				props.signup(), 30, am.usermanagerTableSignup());
		final ColumnConfig<UserVO, Boolean> activeColumn = new ColumnConfig<UserVO, Boolean>(
				props.active(), 25, am.usermanagerTableActive());
		final CheckBoxCell checkboxCell = new CheckBoxCell();
		checkboxCell.setReadOnly(true);
		activeColumn.setCell(checkboxCell);

		final List<ColumnConfig<UserVO, ?>> l = new ArrayList<ColumnConfig<UserVO, ?>>();
		l.add(idColumn);
		l.add(nameColumn);
		l.add(lastNameColumn);
		l.add(emailColumn);
		l.add(signupColumn);
		l.add(activeColumn);

		cm = new ColumnModel<UserVO>(l);
	}

	@UiHandler("addUserButton")
	public void onAddUserButtonClick(final SelectEvent event) {
		presenter.addUser();
	}

	@UiHandler("userListTable")
	public void onGridDoubleClick(final RowDoubleClickEvent event) {
		if (!disableEditFeature) {
			final int row = event.getRowIndex();
			final UserVO user = store.get(row);
			presenter.editUser(user);
		}
	}

	@UiHandler("searchUsersButton")
	public void onSearchUsersButtonClick(final SelectEvent event) {
		presenter.searchUsers();
	}

	public void setDataFilter(
			final GWTPresentationPaginatedDataFilter dataFilter) {
		this.dataFilter = dataFilter;
	}
}