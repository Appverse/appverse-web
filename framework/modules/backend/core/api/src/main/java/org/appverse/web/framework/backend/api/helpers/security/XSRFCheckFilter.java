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

import jodd.typeconverter.Convert;
import jodd.typeconverter.TypeConversionException;
import jodd.util.StringUtil;
import jodd.util.Wildcard;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            try{
                SecurityHelper.checkXSRFToken(req);
            }
            catch (Exception e){
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
