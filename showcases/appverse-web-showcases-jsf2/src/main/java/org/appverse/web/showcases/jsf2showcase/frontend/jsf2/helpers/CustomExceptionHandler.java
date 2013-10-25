/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Mozilla Public 
 License, v. 2.0. If a copy of the MPL was not distributed with this 
 file, You can obtain one at http://mozilla.org/MPL/2.0/. 

 Redistribution and use in source and binary forms, with or without modification, 
 are permitted provided that the conditions of the Mozilla Public License v2.0 
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
package org.appverse.web.showcases.jsf2showcase.frontend.jsf2.helpers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ExceptionHandler exceptionHandler;

	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.exceptionHandler;
	}

	public void handle() throws FacesException {
		for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents()
				.iterator(); i.hasNext();) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			NavigationHandler navigationHandler = facesContext.getApplication()
					.getNavigationHandler();
			ExceptionQueuedEvent exceptionQueuedEvent = i.next();
			ExceptionQueuedEventContext exceptionQueuedEventContext = (ExceptionQueuedEventContext) exceptionQueuedEvent
					.getSource();
			Throwable t = exceptionQueuedEventContext.getException();

			// Get stacktrace
			Writer stacktraceWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stacktraceWriter);
			t.printStackTrace(printWriter);

			// Print stacktrace in logs
			logger.error(stacktraceWriter.toString());
			
			// Get the original cause
			Throwable originalCause = t;
			while (originalCause.getCause() != null){
				originalCause = originalCause.getCause();
			}			
			
			if (t instanceof javax.faces.FacesException) {
				// Treatment for the different kinds of FacesException.
				// Complete here your custom exception handling
				if (originalCause instanceof com.sun.faces.context.FacesFileNotFoundException) {
					// Example: We treat FacesFileNotFoundException as a 404 http error
					navigationHandler.handleNavigation(facesContext, null, "facesNotFound");
				}else if (originalCause instanceof javax.xml.ws.http.HTTPException) {
					// Example: for WS not responding we redirect to a 503 (serviceUnavailable) screen
					navigationHandler.handleNavigation(facesContext, null, "serviceUnavailable");
				} else {
					// Example: Any other faces Exception. In this case we shown a generic error page with the original cause and the stacktrace
					facesContext.getExternalContext().getFlash().put("error", stacktraceWriter.toString());
					if (originalCause != null) {
						facesContext.getExternalContext().getFlash().put("cause", originalCause.getMessage());
					}
					navigationHandler.handleNavigation(facesContext, null,
							"/applicationError");
				}
			} else {
					// Example: Treatment for non FacesException. In this case we shown a generic error page with the original cause and the stacktrace				
					facesContext.getExternalContext().getFlash().put("error", stacktraceWriter.toString());
					if (originalCause != null) {
						facesContext.getExternalContext().getFlash().put("cause", originalCause.getMessage());
					}
					navigationHandler.handleNavigation(facesContext, null,
						"/applicationError");
			}
			i.remove();
		}
		getWrapped().handle();
	}
}