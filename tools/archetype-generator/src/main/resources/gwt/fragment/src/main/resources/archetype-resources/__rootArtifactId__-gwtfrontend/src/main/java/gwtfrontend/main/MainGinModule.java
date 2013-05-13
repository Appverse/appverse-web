package ${package}.gwtfrontend.main;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class MainGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(MainConstants.class).in(Singleton.class);
		bind(MainMessages.class).in(Singleton.class);
		bind(MainImages.class).in(Singleton.class);
	}
}
