/**
 *  Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.
 *
 *  This Source Code Form is subject to the terms of the Appverse Public License
 *  Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this
 *  file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the conditions of the AppVerse Public License v2.0
 *  are met.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 *  SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 *  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package org.test.service.provider.jms;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for creating the tests deployments.
 *
 */
public final class Deployments {

	/**
	 * <p>Creates new instance of {@link Deployments} class.</p>
	 *
	 * <p>Private constructor prevents from instantiation outside of this class.</p>
	 */
	private Deployments() {
	}

	/**
	 * <p>Creates new tests deployment</p>
	 */
	public static WebArchive createDeployment() {

		return ShrinkWrap.create(WebArchive.class, "appverse-messaging-test.war")
				//.addClasses(SampleRepository.class, SampleRepositoryImpl.class)
				.addPackages(true, "org/appverse/web/framework/backend/messaging")
				.addPackages(true, "org/test/app/web/framework/backend/messaging")
				.addAsWebInfResource("test-jms.xml")
				.addAsResource("spring/application-config-async.xml")
				.addAsResource("spring/application-config-sync.xml")
				.addAsResource("spring/application-config-transacted.xml")
				.addAsResource("spring/jms-async-config.xml")
				.addAsResource("spring/jms-common-config.xml")
				.addAsResource("spring/jms-sync-config.xml")
				.addAsResource("spring/jms-transacted-config.xml")
				.addAsResource("log4j/log4j.properties")
				.setWebXML("web.xml")
				.addAsLibraries(springDependencies());

	}

	/**
	 * <p>Retrieves the dependencies.</p>
	 *
	 * @return the array of the dependencies
	 */
	public static File[] springDependencies() {

		List<String> artifacts = new ArrayList<String>();
		artifacts.add("org.springframework:spring-context");
		artifacts.add("org.springframework:spring-jms");
		artifacts.add("org.springframework:spring-web");
		artifacts.add("org.springframework:spring-core");
		artifacts.add("org.springframework:spring-oxm");
		artifacts.add("org.springframework:spring-test");

		artifacts
				.add("org.appverse.web.framework.modules.backend.core.api:appverse-web-modules-backend-core-api");

		artifacts.add("org.slf4j:slf4j-log4j12");

		return Maven.resolver().loadPomFromFile("pom.xml").resolve(artifacts)
				.withTransitivity().asFile();

		/*
				return Maven.resolver().loadPomFromFile("pom.xml")
						.importDependencies(ScopeType.TEST, ScopeType.PROVIDED)
						.resolve().withTransitivity().asFile();
		*/

	}

}
