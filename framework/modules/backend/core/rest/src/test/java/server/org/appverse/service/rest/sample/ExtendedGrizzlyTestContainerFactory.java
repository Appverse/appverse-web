package server.org.appverse.service.rest.sample;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import javax.ws.rs.ProcessingException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

public class ExtendedGrizzlyTestContainerFactory implements TestContainerFactory {

	@Override
	public TestContainer create(final URI baseUri, final ApplicationHandler application)
			throws IllegalArgumentException {
		return new TestContainer() {
			private HttpServer server;

			@Override
			public ClientConfig getClientConfig() {
				return null;
			}

			@Override
			public URI getBaseUri() {
				return baseUri;
			}

			@Override
			public void start() {
				try {
					this.server = GrizzlyWebContainerFactory
							.create(
									baseUri,
									Collections
											.singletonMap(
													"jersey.config.server.provider.packages",
													"server.org.appverse.service.rest.sample.resources;org.appverse.web.framework.backend.rest.services.integration.impl.live")
							);
				} catch (ProcessingException e) {
					throw new TestContainerException(e);
				} catch (IOException e) {
					throw new TestContainerException(e);
				}
			}

			@Override
			public void stop() {
				this.server.stop();
			}
		};

	}

}
