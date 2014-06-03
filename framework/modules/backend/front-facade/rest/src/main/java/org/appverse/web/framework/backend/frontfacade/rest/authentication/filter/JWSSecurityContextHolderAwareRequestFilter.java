package org.appverse.web.framework.backend.frontfacade.rest.authentication.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.appverse.web.framework.backend.frontfacade.rest.authentication.servlet.JWSHttpServletRequestWrapper;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

public class JWSSecurityContextHolderAwareRequestFilter extends GenericFilterBean {
	//~ Instance fields ================================================================================================

	private String rolePrefix;

	//~ Methods ========================================================================================================

	public void setRolePrefix(final String rolePrefix) {
		Assert.notNull(rolePrefix, "Role prefix must not be null");
		this.rolePrefix = rolePrefix.trim();
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res,
			final FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(new JWSHttpServletRequestWrapper((HttpServletRequest) req,
				rolePrefix), res);
	}
}
