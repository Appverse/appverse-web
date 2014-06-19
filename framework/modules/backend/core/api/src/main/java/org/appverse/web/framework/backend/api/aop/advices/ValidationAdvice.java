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
package org.appverse.web.framework.backend.api.aop.advices;

import org.appverse.web.framework.backend.api.aop.advices.technical.AbstractBeforeAOPAdvice;
import org.appverse.web.framework.backend.api.aop.managers.ValidationManager;
import org.appverse.web.framework.backend.api.model.presentation.AbstractPresentationBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.List;

public class ValidationAdvice extends AbstractBeforeAOPAdvice {

	@Autowired
	ValidationManager validationManager;

	public ValidationAdvice() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void before(final Method arg0, final Object[] params, final Object arg2) throws Throwable {

		if (params != null && params.length > 0) {
			if (params[0] instanceof AbstractPresentationBean) {
				validationManager.validateModel((AbstractPresentationBean) params[0]);
			} else if (params[0] instanceof List<?>) {
				List objectList = (List) params[0];
				for (Object element : objectList) {
					if (element instanceof AbstractPresentationBean) {
						validationManager.validateModel((List) params[0]);
					} else {
						throw new Exception("All objects in a save method list need to extend AbstractPresentationBean");
					}
				}
			} else {
				throw new Exception(
						"Method first parameters for save methods needs to extends AbstractPresentationBean or it has to be a list of objects that extend AbstractPresentationBean");
			}
		} else {
			throw new Exception("Method has no parameters");
		}

	}
}