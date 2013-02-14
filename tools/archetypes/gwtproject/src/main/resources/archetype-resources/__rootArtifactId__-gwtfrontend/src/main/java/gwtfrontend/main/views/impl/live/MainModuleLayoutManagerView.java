package ${package}.gwtfrontend.main.views.impl.live;
import org.appverse.web.framework.frontend.gwt.rmvp.ReverseComposite;
import org.appverse.web.framework.frontend.gwt.views.LayoutManagerView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class MainModuleLayoutManagerView extends
			ReverseComposite<LayoutManagerView.LayoutManagerPresenter> implements
			LayoutManagerView {


	interface BodyUiBinder extends UiBinder<Widget, MainModuleLayoutManagerView> {
	}

	private static BodyUiBinder uiBinder = GWT.create(BodyUiBinder.class);

	@UiField
	Viewport panel;

	@Override
	public void changeLayoutWidget(final IsWidget layoutWidget) {
		this.panel.clear();
		this.panel.add(layoutWidget);
	}

	@Override
	public void createView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}