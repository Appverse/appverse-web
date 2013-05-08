package ${package}.gwtfrontend.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(ApplicationGinModule.class)
public interface ApplicationInjector extends Ginjector {

	ApplicationInjector INSTANCE = GWT.create(ApplicationInjector.class);

	ApplicationConstants getApplicationConstants();

	ApplicationImages getApplicationImages();

	ApplicationMessages getApplicationMessages();
}
