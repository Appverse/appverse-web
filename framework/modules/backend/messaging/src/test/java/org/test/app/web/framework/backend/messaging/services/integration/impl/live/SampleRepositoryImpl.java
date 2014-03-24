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
package org.test.app.web.framework.backend.messaging.services.integration.impl.live;

import javax.jms.JMSException;
import javax.jms.Message;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.messaging.services.integration.impl.live.JMSSyncService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Repository;
import org.test.app.web.framework.backend.messaging.model.integration.HeaderDTO;
import org.test.app.web.framework.backend.messaging.model.integration.SampleDTO;
import org.test.app.web.framework.backend.messaging.services.integration.SampleRepository;

/**
 * SampleRepository implementation. 
 * Purpose of this repository is testing message sending and synchronous consuming.
 * In this case, the methods to produce messages accept also a Header.
 *
 */
@Repository
public class SampleRepositoryImpl extends JMSSyncService<SampleDTO> implements SampleRepository {

	@Autowired
	@Qualifier("jmsTemplate")
	JmsTemplate jmsTemplate;

	@AutowiredLogger
	private static Logger logger;

	/* 
	 * Simple method to produce message delegating in JMSService provided methods.
	 */
	@Override
	public void sendSample(final SampleDTO dto) throws Exception
	{
		this.send(dto);
	}

	/* 
	 * Simple method to produce message delegating in JMSService provided methods. It allows sending a generic AbstractIntegrationBean as header.
	 * When using this method, you must overwrite fillHeader (Message, AbstractIntegrationBean) method.
	 */
	@Override
	public void sendSample(final SampleDTO dto, final HeaderDTO header) throws Exception {
		this.send(dto, header);
	}

	/* 
	 * This method is called from JMSTemplate when producing a message -- send(final T dto, final AbstractIntegrationBean header) 
	 */
	@Override
	public void fillHeader(final Message message, final AbstractIntegrationBean header)
			throws JMSException {
		message.setIntProperty("businessId", ((HeaderDTO) header).getSequenceId());
		message.setLongProperty("businessDate", ((HeaderDTO) header).getDate().getTime());
	}

	/* 
	 * Simple method to consume messages asynchronously 
	 */
	@Override
	public SampleDTO retrieveSample() throws Exception {
		return this.syncRetrieve();
	}

	/* 
	 * This method must be overwritten to provide a publishing JmsTemplate
	 */
	@Override
	public JmsTemplate getTemplatePublisher() {
		return jmsTemplate;
	}

	/* 
	 * This method must be overwritten to provide a publishing JmsTemplate
	 */
	@Override
	public JmsTemplate getTemplateConsumer() {
		return jmsTemplate;
	}

}
