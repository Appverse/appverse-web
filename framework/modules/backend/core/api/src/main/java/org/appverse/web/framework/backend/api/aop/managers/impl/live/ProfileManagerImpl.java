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

import org.aopalliance.intercept.MethodInvocation;
import org.appverse.web.framework.backend.api.aop.managers.ProfileManager;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.framework.backend.api.services.integration.AbstractIntegrationService;
import org.appverse.web.framework.backend.api.services.integration.helpers.Mapper;
import org.appverse.web.framework.backend.api.services.presentation.AbstractPresentationService;
import org.slf4j.Logger;

import java.io.*;

public class ProfileManagerImpl implements ProfileManager {
	@AutowiredLogger
	private static Logger logger;

	private boolean showObjects;

	private String getObjectSize(Object object) throws IOException {
		if (object instanceof Serializable) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.close();

			byte[] buf = bos.toByteArray();
			return String.valueOf(buf.length);
		}
		return "Unknow";
	}

	private void logOutput(String type, long timeSpent, String targetClassName,
			String targetMethodName, String targetArgs,
			String targetReturnValue, String targetReturnValueSize,
			Throwable thrown) {

		if (logger.isDebugEnabled()) {
			StringBuilder b = new StringBuilder();
			b.append(type).append("  ");
			b.append(String.format(" Time:%6d ms  ", timeSpent)).append("  ");
			;
			b.append("Class: ").append(targetClassName).append("  ");
			b.append("Method: ").append(targetMethodName).append("  ");
			b.append("ReturnValue: ");
			if (targetReturnValue != null) {
				b.append(targetReturnValue);
			}
			if (targetReturnValueSize != null) {
				b.append(" ReturnSize: ");
				b.append(targetReturnValueSize);
				b.append(" bytes ");
			}

			if (thrown != null) {
				b.append("  EXCEPTION: ").append(thrown.getMessage())
						.append("[").append(thrown.getClass()).append("]");
			}
			logger.debug(b.toString());
		}
	}

	public void setShowObjects(boolean showObjects) {
		this.showObjects = showObjects;
	}

	@Override
	public Object showMethodTimeAndObjects(MethodInvocation mi)
			throws Throwable {
		long tIn = System.currentTimeMillis();
		long tOut = 0;
		String targetArgs = null;
		String targetReturnValue = null;
		String targetReturnValueSize = null;
		Object returnValue = null;
		Throwable thrown = null;

		String classTypeDescription = null;
		if (AbstractPresentationService.class.isAssignableFrom(mi.getThis()
				.getClass())) {
			classTypeDescription = "Presentation";
		} else if (AbstractBusinessService.class.isAssignableFrom(mi.getThis()
				.getClass())) {
			classTypeDescription = "Business";
		} else if (AbstractIntegrationService.class.isAssignableFrom(mi
				.getThis().getClass())
				|| mi.getThis().getClass().isAnnotationPresent(Mapper.class)) {
			classTypeDescription = "Integration";
		} else {
			classTypeDescription = "Unknown Type";
		}

		String targetClassName = ""
				+ (mi.getThis() != null ? mi.getThis().getClass()
						.getSimpleName() : "Unknown Class");
		String targetMethodName = ""
				+ (mi.getMethod() != null ? mi.getMethod().getName()
						: "Unknown Method");
		if (showObjects && logger.isTraceEnabled()) {
			Object[] args = mi.getArguments();
			StringBuffer targetArgsBuffer = new StringBuffer(" Args [");
			for (Object arg : args) {
				targetArgsBuffer.append(arg.toString());
				targetArgsBuffer.append(",");
			}
			targetArgsBuffer.append("]");
			targetArgs = targetArgsBuffer.toString();
		}
		try {
			returnValue = mi.proceed();
			if (showObjects && logger.isTraceEnabled()) {
				targetReturnValue = "ReturnValue [" + returnValue.toString()
						+ "]";
				targetReturnValueSize = getObjectSize(returnValue);
			}
		} catch (Throwable ex) {
			thrown = ex;
		} finally {
			tOut = System.currentTimeMillis();
			logOutput(classTypeDescription, tOut - tIn, targetClassName,
					targetMethodName, targetArgs, targetReturnValue,
					targetReturnValueSize, thrown);
		}
		if (thrown != null) {
			throw thrown;
		} else {
			return returnValue;
		}
	}
}
