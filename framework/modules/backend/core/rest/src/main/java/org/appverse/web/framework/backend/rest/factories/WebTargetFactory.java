/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License 
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this 
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the AppVerse Public License v2.0 
 are met.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) 
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.
 */
package org.appverse.web.framework.backend.rest.factories;

import net.sf.ehcache.Cache;
import org.appverse.web.framework.backend.rest.filters.auth.JWSJerseyFeature;
import org.appverse.web.framework.backend.rest.filters.cache.RestRequestCachingFilter;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.security.Key;

public class WebTargetFactory {

    /**
     * Create a basic non authenticated client
     * @param baseAddress
     * @return webtarget
     */
	public static WebTarget create(final String baseAddress) {

		return WebTargetFactory.create(baseAddress, null);
	}

    /**
     * Create a basic non authenticated client
     * @param baseAddress
     * @param cache
     * @return webtarget
     */
    public static WebTarget create(final String baseAddress, final Cache cache) {
        return WebTargetFactory.create(baseAddress, cache, false);
    }

    public static WebTarget create(final String baseAddress, final Cache cache, Boolean enableBasicAuthenticationFeature) {
        return WebTargetFactory.create(baseAddress, cache, enableBasicAuthenticationFeature, null, null);
    }

    /**
     * Create a JWS authentication client
     * @param baseAddress
     * @param cache
     * @param clientKey
     * @return webtarget
     */
    public static WebTarget create(final String baseAddress, final Cache cache, final Key clientKey) {

        JWSJerseyFeature authFeature =  JWSJerseyFeature
                        .basicBuilder()
                        .credentials(clientKey)
                        .build();
        Client client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .register(LoggingFilter.class)
                .register(authFeature)
                .build();

        if (cache != null)
            client = client.register(new RestRequestCachingFilter(cache));

        WebTarget target = client.target(baseAddress);
        return target;
    }

    /**
     * Create a basic authentication client
     *
     * @param baseAddress
     * @param cache
     * @param enableBasicAuthenticationFeature
     * @param defaultUser
     * @param defaultUserPassword
     * @return webtarget
     */
    public static WebTarget create(final String baseAddress, final Cache cache, Boolean enableBasicAuthenticationFeature, String defaultUser, String defaultUserPassword) {

        // Parameters check
        if (enableBasicAuthenticationFeature != null && enableBasicAuthenticationFeature == true){
            if (defaultUser == null || defaultUserPassword == null) throw
                    new IllegalArgumentException("Basic authentication feature requires arguments 'user' and 'password' to be set up");
        }

        Client client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .build();

        //client = client.property("jersey.config.test.logging.enable", Boolean.TRUE);
        //client = client.property("jersey.config.test.logging.dumpEntity", Boolean.TRUE);
        client = client.register(LoggingFilter.class);

        if (enableBasicAuthenticationFeature != null && enableBasicAuthenticationFeature.booleanValue() == true){
            // Register feature that allows basic authentication (in preemptive and not-preemtive mode)
            // Its use is optional
            final HttpAuthenticationFeature authFeature;
            if (defaultUser != null && defaultUserPassword != null){
                // Default user and password are set (they can be overriden later by request)
                authFeature = HttpAuthenticationFeature
                .basicBuilder()
                .credentials(defaultUser, defaultUserPassword)
                .build();
            }
            else{
                // No default user and password are set (they can be set later by request)
                authFeature = HttpAuthenticationFeature
                        .basicBuilder()
                        .build();
            }
            client.register(authFeature);
        }


        if (cache != null)
            client = client.register(new RestRequestCachingFilter(cache));

        WebTarget target = client.target(baseAddress);
        return target;
    }
}
