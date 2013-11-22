package org.appverse.web.framework.backend.rest.factories;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ServerProperties;

public class WebTargetFactory {

	public static WebTarget create(final String baseAddress) {

		//We need jackson json unmarshaller, since Moxy is unable to deserialize generics collections List<T>
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class)
				.build();

		client = client.property("jersey.config.test.logging.enable", Boolean.TRUE);
		client = client.property("jersey.config.test.logging.dumpEntity", Boolean.TRUE);
		client = client.register(LoggingFilter.class).property(ServerProperties.TRACING,
				"ALL").property(ServerProperties.TRACING_THRESHOLD, "VERBOSE");

		WebTarget target = client.target(baseAddress);
		return target;
	}

}
