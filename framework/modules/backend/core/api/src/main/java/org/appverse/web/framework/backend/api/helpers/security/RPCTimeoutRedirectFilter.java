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
package org.appverse.web.framework.backend.api.helpers.security;

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RPCTimeoutRedirectFilter extends GenericFilterBean
{

	@AutowiredLogger
	private static Logger logger;
	
	private static final String GWT_RPC_CONTENT_TYPE = "text/x-gwt-rpc";

	private final ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();
	private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

	private int customSessionExpiredErrorCode = 901;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain chain) throws IOException, ServletException
	{
		try
		{
			chain.doFilter(request, response);

			logger.debug("Chain processed normally");
		} catch (IOException ex)
		{
			throw ex;
		} catch (Exception ex)
		{
			Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
			RuntimeException ase = (AuthenticationException) throwableAnalyzer
					.getFirstThrowableOfType(AuthenticationException.class, causeChain);

			if (ase == null)
			{
				ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(
						AccessDeniedException.class, causeChain);
			}

			if (ase != null)
			{
				if (ase instanceof AuthenticationException)
				{
					String rpcContentType = ((HttpServletRequest) request)
							.getHeader("Content-Type");

					if (rpcContentType != null && rpcContentType.contains(GWT_RPC_CONTENT_TYPE))
					{
						logger.info("gwt-rpc call detected, send {} error code",
								this.customSessionExpiredErrorCode);
						HttpServletResponse resp = (HttpServletResponse) response;
						resp.sendError(this.customSessionExpiredErrorCode);
					}
					else
					{
						logger.info("Redirect to login page");
						throw ase;
					}
				}
				else if (ase instanceof AccessDeniedException)
				{

					if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext()
							.getAuthentication()))
					{
						logger.info("User session expired or not logged in yet");

                        if (SecurityContextHolder.getContext().getAuthentication() == null){
                            logger.debug("SecurityContextHolder.getContext().getAuthentication() is NULL" +  SecurityContextHolder.getContext().getAuthentication());
                        }
                        else{
                            logger.debug(new StringBuffer().append("SecurityContextHolder.getContext().getAuthentication().getPrincipal(): ").append(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).toString());
                            logger.debug(new StringBuffer().append("SecurityContextHolder.getContext().getAuthentication().getAuthorities(): ").append(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).toString());
                            logger.debug(new StringBuffer().append("SecurityContextHolder.getContext().getAuthentication().getCredentials(): ").append(SecurityContextHolder.getContext().getAuthentication().getCredentials()).toString());
                            logger.debug(new StringBuffer().append("SecurityContextHolder.getContext().getAuthentication().getDetails(): ").append(SecurityContextHolder.getContext().getAuthentication().getDetails()).toString());
                            logger.debug(new StringBuffer().append("SecurityContextHolder.getContext().getAuthentication().isAuthenticated(): ").append(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).toString());
                        }

						String rpcContentType = ((HttpServletRequest) request)
								.getHeader("Content-Type");

						if (rpcContentType != null && rpcContentType.contains(GWT_RPC_CONTENT_TYPE))
						{
							logger.info("gwt-rpc call detected, send {} error code",
									this.customSessionExpiredErrorCode);
							HttpServletResponse resp = (HttpServletResponse) response;
							resp.sendError(this.customSessionExpiredErrorCode);

						}
						else
						{
							logger.info("Redirect to login page");
							throw ase;
						}
					}
					else
					{
						throw ase;
					}
				}
			}

		}
	}

	private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer
	{
		/**
		 * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
		 */
		@Override
		protected void initExtractorMap()
		{
			super.initExtractorMap();

			registerExtractor(ServletException.class, new ThrowableCauseExtractor()
			{
				@Override
				public Throwable extractCause(final Throwable throwable)
				{
					ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
					return ((ServletException) throwable).getRootCause();
				}
			});
		}

	}

	public void setCustomSessionExpiredErrorCode(final int customSessionExpiredErrorCode)
	{
		this.customSessionExpiredErrorCode = customSessionExpiredErrorCode;
	}
}
