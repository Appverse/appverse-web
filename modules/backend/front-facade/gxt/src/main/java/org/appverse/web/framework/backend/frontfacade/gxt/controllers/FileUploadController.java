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
package org.appverse.web.framework.backend.frontfacade.gxt.controllers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.appverse.web.framework.backend.api.services.presentation.IFileUploadPresentationService;
import org.appverse.web.framework.backend.api.services.presentation.PresentationException;
import org.appverse.web.framework.backend.frontfacade.gxt.services.presentation.GWTPresentationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.google.gwt.user.server.rpc.RPC;

@Controller
@RequestMapping(value = "/*/fileupload")
public class FileUploadController {

	@Autowired
	private ApplicationContext applicationContext;

	private final ThreadLocal<String> serviceName = new ThreadLocal<String>();

	private final String MAX_FILE_SIZE_PARAM_NAME = "maxFileSize";

	private void checkXSRFToken(final DefaultMultipartHttpServletRequest request)
			throws IOException {
		String requestValue = request.getParameter("X-XSRF-Cookie");
		String sessionValue = (String) request.getSession().getAttribute(
				"X-XSRF-Cookie");
		if (sessionValue != null && !sessionValue.equals(requestValue)) {
			throw new PreAuthenticatedCredentialsNotFoundException(
					"XSRF attribute not found in session.");
		}
	}

	@RequestMapping(value = "/*.rpc", method = RequestMethod.POST)
	public void handleFormUpload(
			final DefaultMultipartHttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		MultipartFile file = request.getFile("file");
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, String> parameters = new HashMap<String, String>();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			String parameter = request.getParameter(parameterName);
			parameters.put(parameterName, parameter);
		}
		checkXSRFToken(request);

		String path = request.getServletPath();
		serviceName.set(path.substring(path.lastIndexOf('/') + 1,
				path.lastIndexOf('.')));

		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();
			processCall(response, bytes, parameters);
		} else {
			throw new Exception("The file is empty");
		}
	}

	private void processCall(final HttpServletResponse response,
			final byte[] bytes, final Map<String, String> parameters) throws Exception {
		Object presentationService = applicationContext.getBean(serviceName
				.get());
		if (!(presentationService instanceof IFileUploadPresentationService)) {
			throw new IllegalArgumentException(
					"Requested Spring Bean is not a File Upload Presentation Service: ("
							+ presentationService + ")");
		}
		String encodedResult = null;

		if (parameters.get(MAX_FILE_SIZE_PARAM_NAME) != null) {
			long maxFileSize = Long.parseLong(parameters
					.get(MAX_FILE_SIZE_PARAM_NAME));
			if (bytes.length > maxFileSize) {
				encodedResult = RPC.encodeResponseForFailure(
						((IFileUploadPresentationService) presentationService)
								.getClass().getDeclaredMethod("uploadFile",
										bytes.getClass(), Map.class),
						new GWTMaxFileSizeExceedException());
			}
		}

		try {
			String result = ((IFileUploadPresentationService) presentationService)
					.uploadFile(bytes, parameters);
			encodedResult = RPC.encodeResponseForSuccess(
					((IFileUploadPresentationService) presentationService)
							.getClass().getDeclaredMethod("uploadFile",
									bytes.getClass(), Map.class), result);
		} catch (PresentationException e) {
			GWTPresentationException pex = new GWTPresentationException(e);
			encodedResult = RPC.encodeResponseForFailure(
					((IFileUploadPresentationService) presentationService)
							.getClass().getDeclaredMethod("uploadFile",
									bytes.getClass(), Map.class), pex);
		}
		response.getOutputStream().write(encodedResult.getBytes());
	}
}