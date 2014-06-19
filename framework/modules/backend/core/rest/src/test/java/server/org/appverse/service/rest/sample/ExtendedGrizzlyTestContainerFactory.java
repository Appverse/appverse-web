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
package server.org.appverse.service.rest.sample;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import javax.ws.rs.ProcessingException;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;

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
