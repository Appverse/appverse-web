package org.appverse.web.framework.backend.api.helpers.security;

import jodd.typeconverter.Convert;
import jodd.typeconverter.TypeConversionException;
import jodd.util.StringUtil;
import jodd.util.Wildcard;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This filter checks the XSRF token is present in your request.
 * Otherwise it returns an HTTP 401 - Unauthorized status code.
 *
 * Params initialization and url patterns treatment (matches, nocaches and wildcards) is inspired by jodd.servlet.filter.GzipFilter.java
 * http://jodd.org/doc/htmlstapler/enabling-gzip.html#GZIP-filter
 */
public class XSRFCheckFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (isXsrfCheckEligible(req)){
            if (!checkXSRFToken(req)){
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    protected String[] matches;
    protected String[] excludes;
    protected boolean wildcards;

    /**
     *  Checks whether a XSRF token exist and is valid or not
     * @param request
     * @return
     * @throws IOException
     */
    private boolean checkXSRFToken(final HttpServletRequest request)
            throws IOException {
        String requestValue = request.getHeader("X-XSRF-Cookie");
        HttpSession session =  request.getSession(false);
        String sessionValue = null;
        if (session != null){
                sessionValue = (String) session.getAttribute("X-XSRF-Cookie");
        }
        if (requestValue!=null && sessionValue != null && sessionValue.equals(requestValue)) {
            return true;
        }
        else return false;
    }

    /**
     * Filter initialization.
     */
    public void init(FilterConfig config) throws ServletException {

        try {
            wildcards = Convert.toBooleanValue(config.getInitParameter("wildcards"), false);
        } catch (TypeConversionException ignore) {
            wildcards = false;
        }

        // match string
        String uriMatch = config.getInitParameter("match");

        if ((uriMatch != null) && (uriMatch.equals("*") == false)) {
            matches = StringUtil.splitc(uriMatch, ',');
            for (int i = 0; i < matches.length; i++) {
                matches[i] = matches[i].trim();
            }
        }

        // exclude string
        String uriExclude = config.getInitParameter("exclude");

        if (uriExclude != null) {
            excludes = StringUtil.splitc(uriExclude, ',');
            for (int i = 0; i < excludes.length; i++) {
                excludes[i] = excludes[i].trim();
            }
        }
    }

    public void destroy() {
        matches = null;
        excludes = null;
    }

    /**
     * Determine if uri is eligible for being checked against XSRF attacks
     */
    private boolean isXsrfCheckEligible(HttpServletRequest req) {
        String uri = req.getRequestURI();
        if (uri == null) {
            return false;
        }

        boolean result = false;

        if (matches == null) {							// match=*
            result = true;
        } else {
            if (wildcards) {
                result = Wildcard.matchPathOne(uri, matches) != -1;
            } else {
                for (String match : matches) {
                    if (uri.contains(match)) {
                        result = true;
                        break;
                    }
                }
            }
        }

        if ((result == true) && (excludes != null)) {
            if (wildcards) {
                if (Wildcard.matchPathOne(uri, excludes) != -1) {
                    result = false;
                }
            } else {
                for (String exclude : excludes) {
                    if (uri.contains(exclude)) {
                        result = false;						// excludes founded
                        break;
                    }
                }
            }
        }
        return result;
    }
}
