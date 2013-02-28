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

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.appverse.web.framework.backend.api.aop.managers.ValidationManager;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;
import org.appverse.web.framework.backend.api.services.presentation.PresentationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidationManagerImpl implements ValidationManager {
	@AutowiredLogger
	private static Logger logger;

	@Autowired
	private Validator validator;

	@Override
	public void validateModel(final AbstractPresentationBean model)
			throws Throwable {

		final Set<ConstraintViolation<AbstractPresentationBean>> constraintViolations = validator
				.validate(model);
		final StringBuffer sb = new StringBuffer();
		if (!constraintViolations.isEmpty()) {
			final String sep = System.getProperty("line.separator");
			for (final ConstraintViolation<AbstractPresentationBean> viol : constraintViolations) {
				sb.append("Field:: " + viol.getPropertyPath().toString()
						+ " Error:: " + viol.getMessage() + sep);

			}
			logger.error("Server Validation Error: " + sb.toString());
			throw new PresentationException(sb.toString());
		}
	}

	@Override
	public void validateModel(final List<AbstractPresentationBean> modelList)
			throws Throwable {

		Set<ConstraintViolation<AbstractPresentationBean>> constraintViolations = null;
		String sep = System.getProperty("line.separator");
		for (AbstractPresentationBean model : modelList) {
			if (constraintViolations == null) {
				constraintViolations = validator.validate(model);
			} else {
				constraintViolations.addAll(validator.validate(model));
			}
		}

		final StringBuffer sb = new StringBuffer();
		if (!constraintViolations.isEmpty()) {
			for (final ConstraintViolation<AbstractPresentationBean> viol : constraintViolations) {
				sb.append("Field:: " + viol.getPropertyPath().toString()
						+ " Error:: " + viol.getMessage() + sep);

			}
			logger.error("Server Validation Error: " + sb.toString());
			throw new PresentationException(sb.toString());
		}
	}

}