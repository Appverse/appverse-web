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
package org.appverse.web.framework.frontend.gwt.widgets.search.suggest.impl.gxt;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.*;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import org.appverse.web.framework.backend.api.model.presentation.PresentationDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTAbstractPresentationBean;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedDataFilter;
import org.appverse.web.framework.backend.frontfacade.gxt.model.presentation.GWTPresentationPaginatedResult;
import org.appverse.web.framework.frontend.gwt.callback.AppverseCallback;
import org.appverse.web.framework.frontend.gwt.helpers.filters.GxtPaginationConverter;
import org.appverse.web.framework.frontend.gwt.theme.client.search.SuggestTemplate;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.events.LoadSuggestEvent;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.events.SearchSuggestEvent;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.events.SelectSuggestEvent;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.handlers.LoadSuggestEventHandler;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.handlers.SearchSuggestEventHandler;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.handlers.SelectSuggestEventHandler;
import org.appverse.web.framework.frontend.gwt.widgets.search.suggest.model.SuggestModel;

import java.util.ArrayList;

@Singleton
public class SuggestWidgetImpl<M extends GWTAbstractPresentationBean> extends
		Composite implements IsWidget, LeafValueEditor<M> {

	/*
	 * interface Bundle extends ClientBundle {
	 * 
	 * @Source("Suggest.css") SuggestStyle css(); }
	 * 
	 * public interface SuggestStyle extends CssResource { String searchItem();
	 * }
	 */
	/**
	 * The appearance of the suggest widget
	 */
	public interface SuggestAppearance<M> {
		void render(SafeHtmlBuilder sb, M model, SuggestTemplate<M> template);		
		ImageResource iconSearch();
	}

	public interface SuggestWidgetImplUiBinder extends
			UiBinder<Widget, SuggestWidgetImpl<?>> {
	}

	private final SuggestWidgetImplUiBinder uiBinder = GWT
			.create(SuggestWidgetImplUiBinder.class);

	private HandlerManager handlerManager;

	@UiField(provided = true)
	ListStore<M> listStoreSuggest;

	@UiField(provided = true)
	ListView<M, M> listViewSuggest;

	@UiField(provided = true)
	ComboBox<M> searchText;
	
	private TextButton iconSearch;
	
	@UiField(provided = false)
	HorizontalLayoutContainer horizontalLayoutContainer;

	private M value;

	private String loadDataMethod;
	private String clickSelectionMethod;

    AppverseCallback<GWTPresentationPaginatedResult<M>> callbackSuggest;
	private GWTPresentationPaginatedDataFilter dataFilter;
	private SuggestTemplate<M> template;
	private final SuggestModel<M> props;
	private final M instance;
	private final SuggestAppearance<M> appearance;
	// Model Field to search for
	private String modelSearchField;
	private boolean addSearchIcon = false;
	private boolean ignoreCase = true;
	private boolean forceSelection = false;
	//workaround to solve keypressed event bug
	//http://code.google.com/p/google-web-toolkit/issues/detail?id=72
	private String oldText = "";

	/**
	 * Creates a new Suggest with value providers.
	 * 
	 * @param props
	 *            PropertyAccess marker to access model
	 * @param modelInstance
	 *            instance of model
	 */
	@UiConstructor
	@SuppressWarnings("all")
	public SuggestWidgetImpl(final SuggestModel props, final M modelInstance) {

		this.appearance = GWT
				.<SuggestAppearance> create(SuggestAppearance.class);

		this.props = props;
		this.instance = modelInstance;				
		initWidget();
	}

	public HandlerRegistration addSuggestEventHandler(
			final LoadSuggestEventHandler<M> handler) {
		HandlerRegistration reg = null;
		reg = handlerManager.addHandler(LoadSuggestEvent.TYPE, handler);
		return reg;
	}

	public HandlerRegistration addSuggestEventHandler(
			final SelectSuggestEventHandler<M> handler) {
		HandlerRegistration reg = null;
		reg = handlerManager.addHandler(SelectSuggestEvent.TYPE, handler);
		return reg;
	}
	
	public HandlerRegistration addSuggestEventHandler(
			final SearchSuggestEventHandler handler) {
		HandlerRegistration reg = null;
		reg = handlerManager.addHandler(SearchSuggestEvent.TYPE, handler);
		return reg;
	}	

	public String getClickSelectionMethod() {
		return clickSelectionMethod;
	}

	public GWTPresentationPaginatedDataFilter getDataFilter() {
		dataFilter.resetConditions();

		if ((searchText.getText().trim() != null)
				&& (searchText.getText().trim().length() > 0)) {
			// dataFilter.getValues().add(
			// PresentationDataFilter.WILDCARD_ALL + searchText.getText()
			// + PresentationDataFilter.WILDCARD_ALL);
			// dataFilter.getColumns().add(getModelSearchField());
			// dataFilter.getLikes().add(true);

			if (isIgnoreCase()) {
				dataFilter.addLikeConditionIgnoreCase(
						getModelSearchField(),
						PresentationDataFilter.WILDCARD_ALL
								+ searchText.getText()
								+ PresentationDataFilter.WILDCARD_ALL);
			} else {
				dataFilter.addLikeCondition(
						getModelSearchField(),
						PresentationDataFilter.WILDCARD_ALL
								+ searchText.getText()
								+ PresentationDataFilter.WILDCARD_ALL);
			}
		}

		return dataFilter;
	}

	public String getLoadDataMethod() {
		return loadDataMethod;
	}

	public String getModelSearchField() {
		return modelSearchField;
	}

	public String getText() {
		return searchText.getText();
	}

	private void initLoaders() {
		/**** SUGGEST *****/

		// proxy for getting suggestions from server
		final DataProxy<PagingLoadConfig, PagingLoadResult<M>> proxySuggest = new DataProxy<PagingLoadConfig, PagingLoadResult<M>>() {
			@Override
			public void load(final PagingLoadConfig loadConfig,
					final Callback<PagingLoadResult<M>, Throwable> callback) {
				GWT.log("Proxy load method called");
				// We clean any possible previously selected value
				value = null;
				callbackSuggest = new AppverseCallback<GWTPresentationPaginatedResult<M>>() {
					@Override
					public void onFailure(final Throwable caught) {
						GWT.log("list load failed!! " + caught.getMessage());
					}

					@Override
					@SuppressWarnings("unchecked")
					public void onSuccess(
							final GWTPresentationPaginatedResult<M> result) {
						GWT.log("list loaded!! ");

						final PagingLoadResult<M> loadResult = (PagingLoadResult<M>) GxtPaginationConverter
								.convert(result);
						callback.onSuccess(loadResult);

					}
				};
				loadConfig.setSortInfo(new ArrayList<SortInfo>());
				dataFilter = GxtPaginationConverter.convert(loadConfig);

				// CAUTION
				// You must use "getDataFilter()" to avoid loosing any kind of
				// filter when using toolbar

				handlerManager.fireEvent(new LoadSuggestEvent<M>(
						getDataFilter(), callbackSuggest));

			}
		};

		final PagingLoader<PagingLoadConfig, PagingLoadResult<M>> listLoaderSuggest = new PagingLoader<PagingLoadConfig, PagingLoadResult<M>>(
				proxySuggest);

		final LoadResultListStoreBinding<PagingLoadConfig, M, PagingLoadResult<M>> handlerSuggest = new LoadResultListStoreBinding<PagingLoadConfig, M, PagingLoadResult<M>>(
				listStoreSuggest);

		listLoaderSuggest.addLoadHandler(handlerSuggest);
		searchText.setLoader(listLoaderSuggest);

		/* Combo selection clicked */
		searchText.addBeforeSelectionHandler(new BeforeSelectionHandler<M>() {
			@Override
			public void onBeforeSelection(final BeforeSelectionEvent<M> event) {
				event.cancel();
				M model = searchText.getListView().getSelectionModel()
						.getSelectedItem();
				setValue(model);
				handlerManager.fireEvent(new SelectSuggestEvent<M>(model));
			}
		});

		searchText.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(final BlurEvent event) {
				// If no value has been selected and force selection flag is
				// true, we clear the field
				if (isForceSelection() && value == null) {
					clear();
				}
			}
		});

		/* set to null selected value, since criteria has changed */
		//workaround to solve keypressed event bug. workaround is not documented.
		//http://code.google.com/p/google-web-toolkit/issues/detail?id=72
		searchText.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(final KeyUpEvent event) {
				if (!oldText.equals(searchText.getText()))
					setValue(null);
			}

		});

		/**************************************************************/
		/* setPageSize MUST be called AFTER setting loader in combobox. */
		/**************************************************************/
		searchText.setPageSize(10);

	}

	/**
	 * This method will only create the widgets that are marked as provided. It
	 * does not add the widget to the panel: this is still done by the UI
	 * binder.
	 */
	private void initProvidedWidgets() {

		// list store
		listStoreSuggest = new ListStore<M>(props.id());

		// final Bundle b = GWT.create(Bundle.class);
		// b.css().ensureInjected();

		listViewSuggest = new ListView<M, M>(listStoreSuggest,
				new IdentityValueProvider<M>());

		listViewSuggest.setCell(new AbstractCell<M>() {

			@Override
			public void render(
					final com.google.gwt.cell.client.Cell.Context context,
					final M value, final SafeHtmlBuilder sb) {
				appearance.render(sb, value, template);
			}
		});
		final LabelProvider<M> title = props.label();

		final ComboBoxCell<M> cell = new ComboBoxCell<M>(listStoreSuggest,
				title, listViewSuggest) {
			@Override
			public M selectByValue(final String value) {
				M item = super.selectByValue(value);
				if (item == null) {
					item = instance;
					props.name().setValue(item, value);
				}
				return item;
			}
		};

		searchText = new ComboBox<M>(cell);
		searchText.setHideTrigger(true);
		searchText.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
		searchText.setForceSelection(true);
	}

	public void initWidget() {
		handlerManager = new HandlerManager(this);
		initProvidedWidgets();
		initWidget(uiBinder.createAndBindUi(this));
		initLoaders();
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public boolean isForceSelection() {
		return forceSelection;
	}

	public void setClickSelectionMethod(final String clickSelectionMethod) {
		this.clickSelectionMethod = clickSelectionMethod;
	}

	/**
	 * Convenience function for setting disabled/enabled by boolean.
	 * 
	 * @param enabled
	 *            the enabled state
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		searchText.setEnabled(enabled);
	}

	public void setIgnoreCase(final boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public void setLoadDataMethod(final String loadDataMethod) {
		this.loadDataMethod = loadDataMethod;
	}

	public void setModelSearchField(final String modelSearchField) {
		this.modelSearchField = modelSearchField;
	}

	public void setNumChars(final int minNum) {
		searchText.setMinChars(minNum);
	}

	public void setTemplate(final SuggestTemplate<M> template) {
		this.template = template;
	}

	public void setText(final String text) {
		searchText.setText(text);
	}

	@Override
	public void setValue(final M value) {
		//Bugfix: delegate rendering on LabelProvider
		this.value = value;
		if (value != null)
		{
			String name = props.label().getLabel(value);
			if (name != null) {
				searchText.setText(name);
			}
		}
		this.oldText = searchText.getText();
	}

	/**
	 * Sets whether the combo's value is restricted to one of the values in the
	 * list, false to allow the user to set arbitrary text into the field
	 * (defaults to false).
	 * 
	 * @param forceSelection
	 *            true to force selection
	 */
	public void setForceSelection(final boolean forceSelection) {
		this.forceSelection = forceSelection;
	}

	public void clear() {
		searchText.setText("");
		this.setValue(null);
	}

	@Override
	public M getValue() {
		return value;
	}
	
	public boolean isAddSearchIcon() {
		return addSearchIcon;
	}

	public void setAddSearchIcon(boolean addSearchIcon) {
		this.addSearchIcon = addSearchIcon;
		// optional search icon		
		if (addSearchIcon){
			iconSearch = new TextButton();			
			iconSearch.setIcon(appearance.iconSearch());
			iconSearch.setWidth(appearance.iconSearch().getWidth());
			iconSearch.setHeight(appearance.iconSearch().getHeight());
			iconSearch.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					handlerManager.fireEvent(new SearchSuggestEvent());
				}
			});
			horizontalLayoutContainer.add(iconSearch);
		}
		else{
			iconSearch.removeFromParent();
		}
	}
}