package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.views.impl.gxt;

import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.frontend.gwt.rest.ApplicationRestAsyncCallback;

import com.google.gwt.core.client.Callback;
import com.sencha.gxt.data.shared.loader.DataProxy;

public abstract class RestyGWTProxy<C,D> implements DataProxy<C, D> {

	public abstract void load(C loadConfig, ApplicationRestAsyncCallback<D> callback);
//	@Override

	public final void load(C loadConfig, final Callback<D, Throwable> callback) {
		load(loadConfig, new ApplicationRestAsyncCallback<D>() {

			@Override
			public void onFailure(Throwable reason) {
				callback.onFailure(reason);
			}

			@Override
			public void onSuccess(D result) {
				callback.onSuccess(result);
			}
			
		});
		
	}

}
