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

package org.appverse.web.framework.frontend.gwt.views;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import org.appverse.web.framework.frontend.gwt.rmvp.ReverseComposite;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractEditorValidationView<T, M, E extends Editor<? super M>>
		extends ReverseComposite<T> implements EditorValidationView {

	private ValidatorFactory factory = null;

	protected SimpleBeanEditorDriver<M, E> driver;

	public AbstractEditorValidationView() {

		createDriver();
		factory = Validation.byDefaultProvider().configure()
				.buildValidatorFactory();
	}

	/**
	 * JSR-303 Model validation in client
	 * 
	 * @param model
	 * @param groups
	 * @return
	 */
	public boolean validate(final M model, final Class<?>... groups) {

		boolean validated = false;
		final Validator validator = factory.getValidator();
		final Set<ConstraintViolation<M>> violations = validator.validate(
				model, groups);
		if (!violations.isEmpty()) {
			final List<ConstraintViolation<?>> adaptedViolations = new ArrayList<ConstraintViolation<?>>();
			for (final ConstraintViolation<M> violation : violations) {
				adaptedViolations.add(violation);
			}
			driver.setConstraintViolations(adaptedViolations);
		} else {
			validated = true;
		}
		return validated;
	}

}
