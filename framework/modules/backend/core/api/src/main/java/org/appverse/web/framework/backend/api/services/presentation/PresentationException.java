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
package org.appverse.web.framework.backend.api.services.presentation;

import org.appverse.web.framework.backend.api.common.AbstractException;

import java.util.HashMap;

public class PresentationException extends AbstractException {

	private static final long serialVersionUID = 702073327989744226L;

	public PresentationException() {
		super();
	}

	public PresentationException(HashMap<String, String> parameters) {
		super(parameters);
	}

	public PresentationException(HashMap<String, String> parameters,
			String message) {
		super(parameters, message);
	}

	public PresentationException(HashMap<String, String> parameters,
			String message, Throwable cause) {
		super(parameters, message, cause);
	}

	public PresentationException(HashMap<String, String> parameters,
			Throwable cause) {
		super(parameters, cause);
	}

	public PresentationException(long code) {
		super(code);
	}

	public PresentationException(long code, HashMap<String, String> parameters) {
		super(code, parameters);
	}

	public PresentationException(long code, HashMap<String, String> parameters,
			String message) {
		super(code, parameters, message);
	}

	public PresentationException(long code, HashMap<String, String> parameters,
			String message, Throwable cause) {
		super(code, parameters, message, cause);
	}

	public PresentationException(long code, HashMap<String, String> parameters,
			Throwable cause) {
		super(code, parameters, cause);
	}

	public PresentationException(long code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public PresentationException(long code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public PresentationException(long code, Throwable cause) {
		super(code, cause);
		// TODO Auto-generated constructor stub
	}

	public PresentationException(final Long code,
			final HashMap<String, String> parameters) {
		super(code, parameters);
	}

	public PresentationException(final Long code,
			final HashMap<String, String> parameters, final String message) {
		super(code, parameters, message);
	}

	public PresentationException(final Long code,
			final HashMap<String, String> parameters, final String message,
			final Throwable cause) {
		super(code, parameters, message, cause);
	}

	public PresentationException(final Long code,
			final HashMap<String, String> parameters, final Throwable cause) {
		super(code, parameters, cause);
	}

	public PresentationException(final String message) {
		super(message);
	}

	public PresentationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public PresentationException(final Throwable cause) {
		super(cause);
	}
}