package ${package}.gwtfrontend.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(MainGinModule.class)
public interface MainInjector extends Ginjector {

	MainInjector INSTANCE = GWT.create(MainInjector.class);

	MainConstants getMainConstants();

	MainImages getMainImages();

	MainMessages getMainMessages();
}
