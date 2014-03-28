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
package org.test.app.web.framework.backend.messaging;

import java.util.Calendar;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.integration.test.annotation.SpringConfiguration;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.test.app.web.framework.backend.messaging.model.integration.HeaderDTO;
import org.test.app.web.framework.backend.messaging.model.integration.SampleDTO;
import org.test.app.web.framework.backend.messaging.services.integration.OtherSampleRepository;
import org.test.app.web.framework.backend.messaging.services.integration.SampleRepository;
import org.test.service.provider.jms.Deployments;

/**
 * Tests the {@link *SampleRepositories and *AsyncRepositories} class.
 * AsyncRepositories are MessageListener linked in spring context with MessageListenerContainer
 * MessageListenerContainer's are listening/consuming the same queued where *SampleRepositories are producing to.
 *
 */

@RunWith(Arquillian.class)
@SpringConfiguration("classpath:/spring/application-config-async.xml")
public class AsyncMessageTestCase {

	@AutowiredLogger
	private static Logger logger;

	@Autowired
	private SampleRepository sampleRepository;

	@Autowired
	private OtherSampleRepository otherSampleRepository;

	/**
	 * <p>Creates the test deployment.</p>
	 *
	 * @return the test deployment
	 */
	@Deployment
	public static Archive<?> createTestArchive() {

		return Deployments.createDeployment();
	}

	/**
	 * <Tests the {@link sampleRepository#sendSample(SampleDTO)} method and async consuming
	 *
	 * @throws Exception if any error occurs
	 */
	@Test
	public void testSendAndReceiveDTO() throws Exception {

		logger.info("init testSend");
		SampleDTO dto = new SampleDTO();
		dto.setId(4);
		dto.setForeignKey(5);
		dto.setName("JohnDTOandHeader");
		dto.setSurname("SmithDTOandHeader");

		HeaderDTO header = new HeaderDTO();
		header.setSequenceId(100);
		header.setDate(Calendar.getInstance().getTime());

		sampleRepository.sendSample(dto, header);

		logger.info("message sent");
		//Ensure message listener container consuming message before stopping container
		Thread.sleep(1000);

	}

	/**
	 * 	Tests the {@link otherSampleRepository#sendSample(SampleDTO)} method and async consuming
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSendAndReceiveMessage() throws Exception {

		logger.info("init testSend");
		SampleDTO dto = new SampleDTO();
		dto.setId(4);
		dto.setForeignKey(5);
		dto.setName("JohnMessage");
		dto.setSurname("SmithMessage");

		otherSampleRepository.sendSample(dto);

		logger.info("message sent");
		//Ensure message listener container consuming message before stopping container
		Thread.sleep(1000);

	}

}
