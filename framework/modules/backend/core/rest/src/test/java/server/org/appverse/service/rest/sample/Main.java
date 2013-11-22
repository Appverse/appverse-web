package server.org.appverse.service.rest.sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;

public class Main {

	public static void main(final String[] args) throws IOException {
		final String baseUri = "http://localhost:9998/samples/";
		final Map<String, String> initParams = new HashMap<String, String>();

		initParams.put("jersey.config.server.provider.packages",
				"server.org.appverse.service.rest.sample.resources");

		System.out.println("Starting grizzly");
		HttpServer server = GrizzlyWebContainerFactory.create(baseUri, initParams);

		server.start();
		System.out
				.println(String
						.format("Jersey app started with WADL available at %sapplication.wadl Try out %shelloworld. Hit enter to stop it...",
								baseUri, baseUri));

		System.in.read();
		server.stop();
		System.exit(0);
	}
}
