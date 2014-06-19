package org.appverse.web.framework.backend.frontfacade.rest.authentication.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

public class JWSHttpServletRequestWrapper extends SecurityContextHolderAwareRequestWrapper {

	private Logger logger = LoggerFactory.getLogger(JWSHttpServletRequestWrapper.class);

	private final String body;

	public JWSHttpServletRequestWrapper(final HttpServletRequest request, final String rolePrefix) {
		super(request, rolePrefix);

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();

			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

				char[] charBuffer = new char[128];
				int bytesRead = -1;

				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			logger.error("Error reading the request body...");
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					logger.error("Error closing bufferedReader...");
				}
			}
		}

		body = stringBuilder.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());

		ServletInputStream inputStream = new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};

		return inputStream;
	}
}
