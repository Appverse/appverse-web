/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.test.app.web.framework.backend.rest;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response.Status;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.rest.model.integration.StatusResult;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.test.app.web.framework.backend.rest.model.integration.SampleDTO;
import org.test.app.web.framework.backend.rest.services.integration.SampleRepository;

import server.org.appverse.service.rest.sample.ExtendedGrizzlyTestContainerFactory;
import server.org.appverse.service.rest.sample.SampleRestApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class
 * 
 * We are implementing SampleRepository only to ensure every repository method is tested
 *
 */
@ContextConfiguration(locations = { "classpath:/spring/application-config.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class SamplePostTest extends JerseyTest {

	@Autowired
	@Qualifier("sampleRepositoryJson")
	SampleRepository jsonSampleRepository;

	@Autowired
	@Qualifier("sampleRepositoryXml")
	SampleRepository xmlSampleRepository;

	@AutowiredLogger
	private static Logger logger;

	@Before
	public void init()
	{
	}

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return SampleRestApplication.createApp();
	}

	@Override
	protected void configureClient(final ClientConfig config) {
		//config.register(SampleRestApplication.createMoxyJsonResolver());
		//config.register(ClientConfig.class).register(SampleRepositoryImpl.class);
	}

	@Override
	protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
		return new ExtendedGrizzlyTestContainerFactory();
	}

	@Test
	public void insertSample() throws Exception {
		SampleDTO dto = new SampleDTO(4, 34, "newSample", "newSurname");
		dto = jsonSampleRepository.createSample(dto);
		assertNotNull(dto);
		assertEquals(dto.getId(), 4l);
	}

	@Test
	public void insertSampleStatusReturn() throws Exception {
		SampleDTO dto = new SampleDTO(4, 34, "newSample", "newSurname");
		StatusResult status = jsonSampleRepository.createSampleStatus(dto);

		logger.info("new element URI::" + status.getLocation());
		logger.info("message::" + status.getMessage());

		assertEquals(Status.CREATED.getStatusCode(), status.getStatus());

	}

	@Test
	public void insertSampleXml() throws Exception {
		SampleDTO dto = new SampleDTO(4, 34, "newSample", "newSurname");
		dto = xmlSampleRepository.createSample(dto);
		assertNotNull(dto);
		assertEquals(dto.getId(), 4l);
	}

	@Test
	public void insertSampleStatusReturnXml() throws Exception {
		SampleDTO dto = new SampleDTO(4, 34, "newSample", "newSurname");
		StatusResult status = xmlSampleRepository.createSampleStatus(dto);

		logger.info("new element URI::" + status.getLocation());
		logger.info("message::" + status.getMessage());

		assertEquals(Status.CREATED.getStatusCode(), status.getStatus());

	}

}
