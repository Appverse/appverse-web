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
package org.appverse.web.framework.backend.api.common;

import java.util.HashMap;

public abstract class AbstractException extends Exception {

	private static final long serialVersionUID = -6584979637647322656L;

	private long code;
	private HashMap<String, String> parameters;

	public AbstractException() {
		super();
	}

	public AbstractException(final HashMap<String, String> parameters) {
		super();
		this.parameters = parameters;
	}

	public AbstractException(final HashMap<String, String> parameters,
			final String message) {
		super(message);
		this.parameters = parameters;
	}

	public AbstractException(final HashMap<String, String> parameters,
			final String message, final Throwable cause) {
		super(message, cause);
		this.parameters = parameters;
	}

	public AbstractException(final HashMap<String, String> parameters,
			final Throwable cause) {
		super(cause);
		this.parameters = parameters;
	}

	public AbstractException(final long code) {
		super();
		this.code = code;
	}

	public AbstractException(final long code,
			final HashMap<String, String> parameters) {
		super();
		this.code = code;
		this.parameters = parameters;
	}

	public AbstractException(long code,
			final HashMap<String, String> parameters, final String message) {
		super(message);
		this.code = code;
		this.parameters = parameters;
	}

	public AbstractException(final long code,
			final HashMap<String, String> parameters, final String message,
			final Throwable cause) {
		super(message, cause);
		this.code = code;
		this.parameters = parameters;
	}

	public AbstractException(final long code,
			final HashMap<String, String> parameters, final Throwable cause) {
		super(cause);
		this.code = code;
		this.parameters = parameters;
	}

	public AbstractException(final long code, final String message) {
		super(message);
		this.code = code;
	}

	public AbstractException(final long code, final String message,
			final Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public AbstractException(final long code, final Throwable cause) {
		super(cause);
		this.code = code;
	}

	public AbstractException(final String message) {
		super(message);
	}

	public AbstractException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AbstractException(final Throwable cause) {
		super(cause);
	}

	public Long getCode() {
		return code;
	}

	public HashMap<String, String> getParameters() {
		return parameters;
	}
}