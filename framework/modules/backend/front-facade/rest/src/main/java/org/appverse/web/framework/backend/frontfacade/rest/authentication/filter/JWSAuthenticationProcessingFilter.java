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
import java.io.IOException;

public class JWSAuthenticationProcessingFilter extends GenericFilterBean {

	public static final String JWS_AUTH_HEADER="Authorization";
    public static final String JWS_AUTH_HEADER_TOKEN_MARK="Bearer ";

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private final AuthenticationManager authenticationManager;

	@AutowiredLogger
	private Logger logger;

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
                    logger.debug("JWS Token detected:: {}", token);
                }
				try
				{

					JWSAuthenticationToken authRequest =
							new JWSAuthenticationToken(token);

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

}
