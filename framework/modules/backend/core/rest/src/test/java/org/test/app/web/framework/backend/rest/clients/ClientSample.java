package org.test.app.web.framework.backend.rest.clients;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.glassfish.jersey.server.ClientBinding;

@ClientBinding(configClass = ClientConfig.MyClientSampleConfig.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface ClientSample {
}
