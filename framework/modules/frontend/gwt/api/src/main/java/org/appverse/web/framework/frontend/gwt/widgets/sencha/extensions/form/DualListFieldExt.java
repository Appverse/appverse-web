package org.appverse.web.framework.frontend.gwt.widgets.sencha.extensions.form;

import com.google.gwt.cell.client.Cell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.DualListField;

/**
 * Combines two list view fields and allows selections to be moved between
 * fields either using buttons or by dragging and dropping selections. It
 * extends Sencha GXT DualListField overriding some methods so that you can
 * enable / disable completely the component, including the 'From' and 'To'
 * lists.
 * 
 * @param <M>
 *            the model type
 * @param <T>
 *            the type displayed in the list view
 */
public class DualListFieldExt<M, T> extends DualListField<M, T> {

	public DualListFieldExt(final ListStore<M> fromStore,
			final ListStore<M> toStore,
			final ValueProvider<? super M, T> valueProvider, final Cell<T> cell) {
		super(fromStore, toStore, valueProvider, cell);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		getFromView().setEnabled(enabled);
		getToView().setEnabled(enabled);
	}

	@Override
	protected void onAllLeft() {
		if (isEnabled())
			super.onAllLeft();
	}

	@Override
	protected void onAllRight() {
		if (isEnabled())
			super.onAllRight();
	}

	@Override
	protected void onDown() {
		if (isEnabled())
			super.onDown();
	}

	@Override
	protected void onLeft() {
		if (isEnabled())
			super.onLeft();
	}

	@Override
	protected void onRight() {
		if (isEnabled())
			super.onRight();
	}

	@Override
	protected void onUp() {
		if (isEnabled())
			super.onUp();
	}

}
