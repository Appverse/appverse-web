package ${package}.gwtfrontend.common;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class ApplicationGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(ApplicationConstants.class).in(Singleton.class);
		bind(ApplicationMessages.class).in(Singleton.class);
		bind(ApplicationImages.class).in(Singleton.class);
	}

}
