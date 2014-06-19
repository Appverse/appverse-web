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
package org.appverse.web.framework.backend.api.controllers;

import org.appverse.web.framework.backend.api.helpers.security.SecurityHelper;
import org.appverse.web.framework.backend.api.model.presentation.FileVO;
import org.appverse.web.framework.backend.api.services.presentation.IFileRetrievalPresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/*/fileretrieval")
public class FileRetrievalController {

	@Autowired
	private ApplicationContext applicationContext;

	private final ThreadLocal<String> serviceName = new ThreadLocal<String>();

	// private static final int MAX_FILE_SIZE = 1024 * 1024 * 2;

	@RequestMapping(value = "/*.rpc", method = RequestMethod.POST)
	public void handleFormUpload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		@SuppressWarnings("unchecked")
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, String> parameters = new HashMap<String, String>();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			String parameter = request.getParameter(parameterName);
			parameters.put(parameterName, parameter);
		}
		SecurityHelper.checkXSRFToken(request);

		String path = request.getServletPath();
		serviceName.set(path.substring(path.lastIndexOf('/') + 1,
				path.lastIndexOf('.')));
		processCall(response, parameters);

	}

	private void processCall(HttpServletResponse response,
			Map<String, String> parameters) throws Exception {
		Object presentationService = applicationContext.getBean(serviceName
				.get());
		if (!(presentationService instanceof IFileRetrievalPresentationService)) {
			throw new IllegalArgumentException(
					"Requested Spring Bean is not a File Retrieval Presentation Service: ("
							+ presentationService + ")");
		}

		FileVO fileVO = ((IFileRetrievalPresentationService) presentationService)
				.retrievalFile(parameters);
		final ServletOutputStream output = response.getOutputStream();
		response.setContentType(fileVO.getMimeType());
		response.setHeader("Content-disposition", "attachment; filename=\""
				+ fileVO.getFilename() + "\"");
		output.write(fileVO.getBytes());
		output.close();

	}
}