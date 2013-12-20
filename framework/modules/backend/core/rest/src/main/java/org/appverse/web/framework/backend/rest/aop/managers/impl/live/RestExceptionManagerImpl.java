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
package org.appverse.web.framework.backend.rest.aop.managers.impl.live;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.rest.aop.managers.RestExceptionManager;
import org.appverse.web.framework.backend.rest.exceptions.RestWebAppException;
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse;
import org.slf4j.Logger;

/**
 * This class manages the error handling of rest integration services
 * Any JAX-RS WebApplicationException is encapsulated into RestWebAppException and propaged 
 *
 */
public class RestExceptionManagerImpl implements RestExceptionManager {

	@AutowiredLogger
	private static Logger logger;

	@Override
	public void convertAndRethrowException(final Method method,
			final Object[] args, final Object target, final Throwable ex)
			throws Throwable {

		if (ex instanceof WebApplicationException)
		{

			WebApplicationException wae = (WebApplicationException) ex;
			Response response = wae.getResponse();
			if (response instanceof OutboundJaxrsResponse)
			{
				throw new RestWebAppException("", response.getStatus(), wae);
			}
			String reason = response.readEntity(String.class);
			RestWebAppException rex = new RestWebAppException(reason, response.getStatus(), wae);
			throw rex;
		}
		else if (ex instanceof InvocationTargetException)
		{
			InvocationTargetException ite = (InvocationTargetException) ex;
			Throwable targetEx = ite.getTargetException();
			if (targetEx != null)
				convertAndRethrowException(method, args, target, targetEx);
			else
				throw ex;
		}
		else
			throw ex;

	}
}
