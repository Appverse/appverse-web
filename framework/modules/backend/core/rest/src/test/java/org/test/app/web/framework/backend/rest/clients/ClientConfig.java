package org.test.app.web.framework.backend.rest.clients;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;

public class ClientConfig extends ResourceConfig {

	private static String baseUriClientSample;

	public static class MyClientSampleConfig extends ClientConfig {
		public MyClientSampleConfig() {
			//this.register(new CustomHeaderFilter("custom-header", "b"));

			this.property(ClientSample.class.getName() + ".baseUri", baseUriClientSample);

			//.property(ClientA.class.getName() + ".baseUri", BASE_URI.toString() + "internal");

		}
	}

	@Value("${client.sample.baseUri}")
	public void setBaseUriClientSample(final String baseUriClientSample) {
		ClientConfig.baseUriClientSample = baseUriClientSample;
	}

}
