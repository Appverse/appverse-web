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
package org.appverse.web.framework.backend.messaging.services.integration;

import org.appverse.web.framework.backend.api.model.integration.AbstractIntegrationBean;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Interface to define Asynch Message consuming API
 *
 * @param <T>
 */
public interface IJMSServiceAsyncConsumer<T extends AbstractIntegrationBean> extends
		IJMSService<T>,
		MessageListener {

	/**
	 * 
	 * Overwrite this method to process message consumed as DTO
	 * MessageConversion is done within its implemetation 
	 * 
	 * @param dto
	 * @throws Exception
	 */
	void processMessage(T dto) throws Exception;

	/**
	 * 
	 * Overwrite this method to process message consumed as JMS Message 
	 * It allows to process JMS Properties.
	 * 
	 * @param message
	 * @throws Exception
	 */
	void processMessage(final Message message) throws Exception;

	/**
	 * Overwrite to provide message converter implementation
	 * 
	 * @return
	 */
	MessageConverter getMessageConverter();

}
