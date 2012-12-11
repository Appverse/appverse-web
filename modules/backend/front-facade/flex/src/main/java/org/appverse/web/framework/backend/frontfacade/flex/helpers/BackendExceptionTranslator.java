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
package org.appverse.web.framework.backend.frontfacade.flex.helpers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import org.appverse.web.framework.backend.api.common.AbstractException;
import org.springframework.flex.core.ExceptionTranslator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;

import flex.messaging.MessageException;

public class BackendExceptionTranslator implements ExceptionTranslator {

	// private static String TECHNICAL_SAVE_CONCURRENT_ERROR = "B001";

	// private static String TECHNICAL_SAVE_CONCURRENT_MSG =
	// "Error saving data: concurrent change. Please refresh your data and try again";

	private static String TECHNICAL_SAVE_INVALID_ERROR = "B002";

	private static String TECHNICAL_SAVE_INVALID_MSG = "Error saving data: invalid data";

	// private static String AUTHORIZATION_EXCEPTION_ERROR = "B101";

	// private static String AUTHORIZATION_EXCEPTION_MSG =
	// "Session has expired. Please refresh your browser";

	private static String BUSINESS_ERROR = "B501";

	private static String UNEXPECTED_ERROR = "B999";

	private static String UNEXPECTED_MSG = "Unexpected error. Please contact your administrator.";

	private static String BAD_CREDENTIALS_ERROR = "BS01";

	private static String BAD_CREDENTIALS_MSG = "Information username or password you entered is incorrect";

	private static String ACCESS_DENIED_ERROR = "BS02";

	private static String ACCESS_DENIED_MSG = "Access to requested service is denied";

	@Override
	public boolean handles(final Class<?> arg0) {
		return true;
	}

	public void setMessage(final String message, final Throwable throwable,
			final MessageException exception) {

		final StringBuffer sbf = new StringBuffer(message);

		String technicalMessage = null;
		Throwable current = throwable;
		while (technicalMessage == null && current != null) {
			if (current instanceof SQLException) {
				technicalMessage = current.toString();
			}
			current = current.getCause();
		}

		if (technicalMessage != null) {
			sbf.append(" [");
			sbf.append(technicalMessage);
			sbf.append("]");
		}

		exception.setMessage(sbf.toString());
	}

	@Override
	public MessageException translate(final Throwable throwable) {
		final MessageException exception = new MessageException();

		try {
			/*
			 * if (throwable instanceof AuthorizationException) { exception
			 * .setCode
			 * (BackendExceptionTranslator.AUTHORIZATION_EXCEPTION_ERROR);
			 * setMessage(
			 * BackendExceptionTranslator.AUTHORIZATION_EXCEPTION_MSG,
			 * throwable, exception); } else
			 */if (throwable instanceof AbstractException) {
				exception.setCode(BackendExceptionTranslator.BUSINESS_ERROR);
				setMessage(throwable.getMessage(), throwable, exception);
			} else if (throwable instanceof AccessDeniedException) {
				exception
						.setCode(BackendExceptionTranslator.ACCESS_DENIED_ERROR);
				setMessage(BackendExceptionTranslator.ACCESS_DENIED_MSG,
						throwable, exception);
			} else if (throwable instanceof BadCredentialsException) {
				exception
						.setCode(BackendExceptionTranslator.BAD_CREDENTIALS_ERROR);
				setMessage(BackendExceptionTranslator.BAD_CREDENTIALS_MSG,
						throwable, exception);
			}
			// else if (throwable instanceof
			// JpaOptimisticLockingFailureException) {
			// exception
			// .setCode(BackendExceptionTranslator.TECHNICAL_SAVE_CONCURRENT_ERROR);
			// setMessage(
			// BackendExceptionTranslator.TECHNICAL_SAVE_CONCURRENT_MSG,
			// throwable, exception);
			// }
			else if (throwable instanceof TransactionSystemException) {
				exception
						.setCode(BackendExceptionTranslator.TECHNICAL_SAVE_INVALID_ERROR);
				setMessage(
						BackendExceptionTranslator.TECHNICAL_SAVE_INVALID_MSG,
						throwable, exception);
			} else {
				throwable.printStackTrace();
				exception.setCode(BackendExceptionTranslator.UNEXPECTED_ERROR);
				setMessage(BackendExceptionTranslator.UNEXPECTED_MSG,
						throwable, exception);
			}
			final StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			exception.setDetails(sw.toString());
		} catch (final Throwable th) {
			th.printStackTrace();
			exception.setCode(BackendExceptionTranslator.UNEXPECTED_ERROR);
			exception.setMessage(BackendExceptionTranslator.UNEXPECTED_MSG);
		}

		return exception;
	}
}