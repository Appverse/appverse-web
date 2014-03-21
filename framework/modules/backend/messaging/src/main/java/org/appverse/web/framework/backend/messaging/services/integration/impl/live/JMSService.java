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

import java.util.Enumeration;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.api.services.integration.AbstractIntegrationService;
import org.appverse.web.framework.backend.messaging.services.integration.IJMSService;
import org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceAsyncConsumer;
import org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceConsumer;
import org.appverse.web.framework.backend.messaging.services.integration.IJMSServicePublisher;
import org.slf4j.Logger;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * Implementation to provide integration with JMS broker
 *
 * @param <T>
 */
public abstract class JMSService<T extends AbstractIntegrationBean>
		extends AbstractIntegrationService<T> implements IJMSService<T> {

	@AutowiredLogger
	private static Logger logger;

	/**
	 * @param dto
	 * @throws Exception
	 */
	public final void send(final T dto) throws Exception {
		if (this instanceof IJMSServicePublisher)
		{
			((IJMSServicePublisher<?>) this).getTemplatePublisher().convertAndSend(dto,
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
		else
			throw new UnsupportedOperationException("You must implement 'IJMSServicePublisher");
	}

	/*
	 * Publish JMS message with header data
	 * 
	 */
	public final void send(final T dto, final AbstractIntegrationBean header) throws Exception {

		if (this instanceof IJMSServicePublisher)
		{
			((IJMSServicePublisher<?>) this).getTemplatePublisher().convertAndSend(dto,
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
		else
			throw new UnsupportedOperationException("You must implement 'IJMSServicePublisher");

	}

	/**
	 * This method should be overridden in repositories implementations
	 * 
	 * @param message
	 * @param header
	 */
	public void fillHeader(final Message message, final AbstractIntegrationBean header)
			throws JMSException
	{
		throw new UnsupportedOperationException(
				"You must overwrite 'fillHeader(final Message message, final AbstractIntegrationBean header)' method");
	}

	public final void onMessage(final Message msg) {

		try
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("***** ASYNC CONSUMING MESSAGE******");
				traceMessage(msg);
			}
			this.processMessage(msg);
			//Define behaviour
		} catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}

	}

	public void processMessage(final Message message) throws Exception {
		Object dto = ((IJMSServiceAsyncConsumer<?>) this).getMessageConverter()
				.fromMessage(message);
		this.processMessage((T) dto);
	}

	public void processMessage(final T dto) throws Exception {
		throw new UnsupportedOperationException(
				"You must overwrite 'processMessage(final T dto)' method");
	}

	@SuppressWarnings("unchecked")
	public final T syncRetrieve() throws Exception
	{
		if (this instanceof IJMSServiceConsumer)
		{
			if (logger.isDebugEnabled())
			{
				Message msg = ((IJMSServiceConsumer<?>) this).getTemplateConsumer().receive();

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
		else
			throw new UnsupportedOperationException("You must implement 'IJMSServiceConsumer");
	}

	@SuppressWarnings("unchecked")
	public final T syncRetrieve(final String messageSelector) throws Exception
	{
		if (this instanceof IJMSServiceConsumer)
		{
			if (logger.isDebugEnabled())
			{
				Message msg = ((IJMSServiceConsumer<?>) this).getTemplateConsumer().receive(
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
		else
			throw new UnsupportedOperationException("You must implement 'IJMSServiceConsumer");
	}

	/**
	 * Trace message body and all its JMS properties
	 * 
	 * @param msg
	 * @throws JMSException
	 */
	protected final void traceMessage(final Message msg) throws JMSException
	{
		if (msg instanceof BytesMessage) {
			BytesMessage messageBody = (BytesMessage) msg;
			// message is in write mode, close & reset to start
			// of byte stream
			messageBody.reset();
			Long length = messageBody.getBodyLength();
			if (logger.isDebugEnabled())
				logger.debug("***** MESSAGE LENGTH BytesMessage is {} bytes", length);
			byte[] byteMyMessage = new byte[length.intValue()];
			messageBody.readBytes(byteMyMessage);
			if (logger.isDebugEnabled())
				logger.debug("***** TRACE MESSAGE - \n<!-- MSG START -->\n{}\n<!-- MSG END -->",
						new String(
								byteMyMessage));
			messageBody.reset();

		}
		else if (msg instanceof TextMessage)
		{
			TextMessage textMsg = (TextMessage) msg;
			String body = textMsg.getText();
			if (logger.isDebugEnabled())
				logger.debug("***** MESSAGE LENGTH TextMessage is {} bytes", body.length());
			if (logger.isDebugEnabled())
				logger.debug("***** TRACE MESSAGE - \n<!-- MSG START -->\n{}\n<!-- MSG END -->",
						body);

		}
		else if (msg instanceof ObjectMessage)
		{
			ObjectMessage oMsg = (ObjectMessage) msg;
			if (logger.isDebugEnabled())
				logger.debug("***** Binary Message ObjectMessage ******");
			if (logger.isDebugEnabled())
				logger.debug("***** TRACE MESSAGE - \n<!-- MSG START -->\n{}\n<!-- MSG END -->",
						oMsg.toString());

		}

		@SuppressWarnings("unchecked")
		Enumeration<String> en = msg.getPropertyNames();
		while (en.hasMoreElements())
		{
			String key = en.nextElement();
			Object value = msg.getObjectProperty(key);
			if (logger.isDebugEnabled())
				logger.debug("header key: " + key + " -- value: " + value.toString());

		}
		if (logger.isDebugEnabled())
		{
			logger.debug("header key: messageId-- value: " + msg.getJMSMessageID());
			logger.debug("header key: correlationId-- value: " + msg.getJMSCorrelationID());
			logger.debug("header key: expiration-- value: " + msg.getJMSExpiration());
			logger.debug("header key: priority-- value: " + msg.getJMSPriority());
			logger.debug("header key: timestamp-- value: " + msg.getJMSTimestamp());
			logger.debug("header key: type-- value: " + msg.getJMSType());
			logger.debug("header key: destination-- value: " + msg.getJMSDestination());
			logger.debug("header key: redelivered-- value: " + msg.getJMSRedelivered());
			logger.debug("header key: replyTo-- value: " + msg.getJMSReplyTo());
		}

	}
}
