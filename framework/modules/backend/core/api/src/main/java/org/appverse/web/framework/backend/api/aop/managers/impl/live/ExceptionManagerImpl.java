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
package org.appverse.web.framework.backend.api.aop.managers.impl.live;

import org.appverse.web.framework.backend.api.aop.managers.ExceptionManager;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.framework.backend.api.services.business.BusinessException;
import org.appverse.web.framework.backend.api.services.integration.AbstractIntegrationService;
import org.appverse.web.framework.backend.api.services.integration.IntegrationException;
import org.appverse.web.framework.backend.api.services.presentation.AbstractPresentationService;
import org.appverse.web.framework.backend.api.services.presentation.PresentationException;
import org.slf4j.Logger;

import java.lang.reflect.Method;

public class ExceptionManagerImpl implements ExceptionManager {

	@AutowiredLogger
	private static Logger logger;

	@Override
	public void logAndRethrowException(final Method method,
			final Object[] args, final Object target, final Throwable ex)
			throws Throwable {

		if (target instanceof AbstractIntegrationService) {
			logger.error(
					"Integration Exception Executing Service: "
							+ target.getClass().getSimpleName() + " Method: "
							+ method.getName(), ex);
			if (!(ex instanceof IntegrationException)) {
				throw new IntegrationException(ex);
			} else {
				throw ex;
			}
		} else if (target instanceof AbstractBusinessService) {
			if (!(ex instanceof IntegrationException)) {
				logger.error("Business Exception Executing Service: "
						+ target.getClass().getSimpleName() + " Method: "
						+ method.getName(), ex);
			}
			if (ex instanceof IntegrationException) {
				IntegrationException iex = (IntegrationException) ex;
				BusinessException bex = new BusinessException(iex.getCode(),
						iex.getParameters(), ex);
				bex.setStackTrace(iex.getStackTrace());
				throw bex;
			} else if (ex instanceof BusinessException) {
				throw ex;
			} else {
				BusinessException bex = new BusinessException(ex);
				bex.setStackTrace(ex.getStackTrace());
			}
		} else if (target instanceof AbstractPresentationService) {
			if (!(ex instanceof BusinessException)) {
				logger.error("Presentation Exception Executing Service: "
						+ target.getClass().getSimpleName() + " Method: "
						+ method.getName(), ex);
			}
			if (ex instanceof BusinessException) {
				BusinessException bex = (BusinessException) ex;
				PresentationException pex = new PresentationException(
						bex.getCode(), bex.getParameters(), ex);
				pex.setStackTrace(bex.getStackTrace());
				throw pex;
			} else if (ex instanceof PresentationException) {
				throw ex;
			} else {
				throw new PresentationException(ex.getMessage(), ex);
			}
		}
	}
}