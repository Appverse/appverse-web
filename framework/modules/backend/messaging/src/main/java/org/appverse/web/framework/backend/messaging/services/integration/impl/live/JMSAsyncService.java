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

import javax.jms.Message;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceAsyncConsumer;
import org.slf4j.Logger;

/**
 * Implementation to provide integration with JMS broker through JMS MessageListener
 *
 * @param <T>
 */
public abstract class JMSAsyncService<T extends AbstractIntegrationBean>
		extends JMSService<T> implements IJMSServiceAsyncConsumer<T> {

	@AutowiredLogger
	private static Logger logger;

	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceAsyncConsumer#processMessage(javax.jms.Message)
	 */
	@Override
	public void processMessage(final Message message) throws Exception {
		Object dto = ((IJMSServiceAsyncConsumer<?>) this).getMessageConverter()
				.fromMessage(message);
		this.processMessage((T) dto);
	}

	/* (non-Javadoc)
	 * @see org.appverse.web.framework.backend.messaging.services.integration.IJMSServiceAsyncConsumer#processMessage(org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean)
	 */
	@Override
	public void processMessage(final T dto) throws Exception {
		throw new UnsupportedOperationException(
				"You must overwrite 'processMessage(final T dto)' method");
	}

}
