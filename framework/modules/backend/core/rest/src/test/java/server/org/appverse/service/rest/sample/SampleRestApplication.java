package server.org.appverse.service.rest.sample;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import server.org.appverse.service.rest.sample.resources.MockResource;
import server.org.appverse.service.rest.sample.resources.WebApplicationExceptionMapper;

import javax.ws.rs.ext.ContextResolver;

public class SampleRestApplication {

	//private static final URI BASE_URI = URI.create("http://localhost:9998/jsonmoxy/");

	public static ResourceConfig createApp() {
		return new ResourceConfig(MockResource.class).register(LoggingFilter.class)
				.register(WebApplicationExceptionMapper.class)
				.register(JacksonFeature.class)
				.property(ServerProperties.TRACING,
						"ALL").property(ServerProperties.TRACING_THRESHOLD, "VERBOSE");

		//register(createMoxyJsonResolver().pr);
		//packages("server.org.appverse.service.rest.sample.resources");

	}

	public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
		final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
		//Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
		//namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		//moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
		return moxyJsonConfig.resolver();
	}

}
