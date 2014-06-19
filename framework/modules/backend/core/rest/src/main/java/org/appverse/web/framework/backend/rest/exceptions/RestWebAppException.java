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
package org.appverse.web.framework.backend.rest.exceptions;

import org.appverse.web.framework.backend.api.services.integration.IntegrationException;

import java.util.HashMap;

/**
 * Appverse Integration Exception in charge of encapsulating JAX-RS service exceptions.
 *
 */
public class RestWebAppException extends IntegrationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1947121292176177735L;
	private final String reason;
	private final int statusCode;

	public RestWebAppException(final String reason, final int statusCode) {

		super(statusCode, RestWebAppException.getParams(reason, statusCode), reason);
		this.reason = reason;
		this.statusCode = statusCode;

	}

	public RestWebAppException(final String reason, final int statusCode, final Throwable t) {

		super(statusCode, RestWebAppException.getParams(reason, statusCode), reason, t);
		this.reason = reason;
		this.statusCode = statusCode;

	}

	public static HashMap<String, String> getParams(final String reason, final int statusCode)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("statusCode", String.valueOf(statusCode));
		map.put("reason", reason);
		return map;
	}

	public String getReason() {
		return reason;
	}

	public int getStatusCode() {
		return statusCode;
	}

	//Getters and setters
}
