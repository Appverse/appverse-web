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
package org.appverse.web.framework.backend.messaging.services.integration.impl.live;

import javax.jms.JMSException;
import javax.jms.Message;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceConsumer;
import org.appverse.web.framework.backend.messaging.services.integration.IJMSServicePublisher;
import org.slf4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * Implementation to provide integration with JMS broker through JMSTemplate
 *
 * @param <T>
 */
public abstract class JMSSyncService<T extends AbstractIntegrationBean>
		extends JMSService<T> implements IJMSServicePublisher<T>, IJMSServiceConsumer<T> {

	@AutowiredLogger
	private static Logger logger;

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServicePublisher#send(org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean)
	 */
	@Override
	public void send(final T dto) throws Exception {
		this.getTemplatePublisher().convertAndSend(dto,
				new MessagePostProcessor() {
					@Override
					public Message postProcessMessage(final Message message)
							throws JMSException {
						if (logger.isDebugEnabled())
						{
							logger.debug("***** SENDING MESSAGE ******");
							traceMessage(message);
						}
						return message;
					}
				});
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServicePublisher#send(org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean)
	 */
	@Override
	public void send(final T dto, final AbstractIntegrationBean header) throws Exception {

		this.getTemplatePublisher().convertAndSend(dto,
				new MessagePostProcessor() {
					@Override
					public Message postProcessMessage(final Message message)
							throws JMSException {
						// overwrite in Repositories
						fillHeader(message, header);
						if (logger.isDebugEnabled())
						{
							logger.debug("***** SENDING MESSAGE WITH HEADER******");
							traceMessage(message);
						}
						return message;
					}
				});

	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServicePublisher#fillHeader(javax.jms.Message, org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean)
	 */
	@Override
	public void fillHeader(final Message message, final AbstractIntegrationBean header)
			throws JMSException
	{
		throw new UnsupportedOperationException(
				"You must overwrite 'fillHeader(final Message message, final AbstractIntegrationBean header)' method");
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceConsumer#syncRetrieve()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T syncRetrieve() throws Exception
	{
		if (logger.isDebugEnabled())
		{
			Message msg = this.getTemplateConsumer().receive();

			logger.debug("***** SYNC CONSUMING MESSAGE******");
			traceMessage(msg);

			MessageConverter mc = ((IJMSServiceConsumer<?>) this).getTemplateConsumer()
					.getMessageConverter();
			return (T) mc.fromMessage(msg);
		}
		else
			return (T) ((IJMSServiceConsumer<?>) this).getTemplateConsumer()
					.receiveAndConvert();
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceConsumer#syncRetrieve(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T syncRetrieve(final String messageSelector) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			Message msg = this.getTemplateConsumer().receive(
					messageSelector);

			logger.debug("***** SYNC CONSUMING MESSAGE - SELECTOR ******");
			traceMessage(msg);

			MessageConverter mc = ((IJMSServiceConsumer<?>) this).getTemplateConsumer()
					.getMessageConverter();
			return (T) mc.fromMessage(msg);
		}
		else
			return (T) ((IJMSServiceConsumer<?>) this).getTemplateConsumer()
					.receiveAndConvert(messageSelector);
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServicePublisher#getTemplatePublisher()
	 */
	@Override
	public JmsTemplate getTemplatePublisher() {
		throw new UnsupportedOperationException("You must implement 'getTemplatePublisher");
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceConsumer#getTemplateConsumer()
	 */
	@Override
	public JmsTemplate getTemplateConsumer() {
		throw new UnsupportedOperationException("You must implement 'getTemplateConsumer");
	}

}
