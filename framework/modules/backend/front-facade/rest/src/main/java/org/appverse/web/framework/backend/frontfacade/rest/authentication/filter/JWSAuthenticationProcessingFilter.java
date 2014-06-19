package org.appverse.web.framework.backend.frontfacade.rest.authentication.filter;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.frontfacade.rest.authentication.model.JWSAuthenticationToken;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class JWSAuthenticationProcessingFilter extends GenericFilterBean {

	public static final String JWS_AUTH_HEADER="Authorization";
    public static final String JWS_AUTH_HEADER_TOKEN_MARK="Bearer ";

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private final AuthenticationManager authenticationManager;

	@AutowiredLogger
	private Logger logger;

	public static final int PAYLOAD_HEADER_MAX_SIZE = 1024;

	public JWSAuthenticationProcessingFilter(final AuthenticationManager authenticationManager) {

		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String authHeader = req.getHeader(JWS_AUTH_HEADER);
		if (!StringUtils.isEmpty(authHeader))
		{
			int pos = authHeader.indexOf(JWS_AUTH_HEADER_TOKEN_MARK);
			if (pos != -1)
			{
				String token = authHeader.substring(pos + JWS_AUTH_HEADER_TOKEN_MARK.length());
                if(logger.isDebugEnabled()){
                    logger.debug("JWS Token detected: {}", token);
                }
				try
				{
					InputStream stream = req.getInputStream();
					StringBuilder messagePayload = readInputStream(stream);

					if (logger.isDebugEnabled())
						logger.debug("payload: {}", messagePayload.toString());

					if (StringUtils.isEmpty(messagePayload.toString())) {
                        try {
                            URI url = new URI(req.getRequestURL().toString());
                            messagePayload = new StringBuilder(url.getPath());
                        }catch(URISyntaxException ure){
                            messagePayload = new StringBuilder();
                        }

                    }else
					{
						//There is a short limitation for http headers. It depends on server.
						//As message payload grows, payload in header is growing too, so we must set a limit.
						//It means that we are only signing, validating and checking message integrity of first 1024 characters
						//Same logic is applied in client side						
						if (messagePayload.length() > PAYLOAD_HEADER_MAX_SIZE)
							messagePayload = new StringBuilder(messagePayload.substring(0,
									PAYLOAD_HEADER_MAX_SIZE));
					}

					JWSAuthenticationToken authRequest =
							new JWSAuthenticationToken(token, messagePayload);

					authRequest.setDetails(authenticationDetailsSource.buildDetails(req));

					Authentication authResult = authenticationManager.authenticate(authRequest);

					SecurityContextHolder.getContext().setAuthentication(authResult);

					chain.doFilter(request, response);
					return;

				} catch (AuthenticationException e){
					logger.error("Exception authenticating request", e);
				}
			}

		}

		res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		/*
		authenticationEntryPoint.commence(req, res, new BadCredentialsException("error"));
		*/

	}

	private StringBuilder readInputStream(final InputStream stream)
	{
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			if (stream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(stream));

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
		return stringBuilder;

	}
}
