package org.appverse.web.framework.backend.frontfacade.rest.authentication.filter;/*
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

import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Add headers to prevent Cross-site ajax calls issues
 */
public class XSSHeaderFilter extends GenericFilterBean {

    public static final String REQUEST_ORIGIN = "Origin";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_DEFAULT = "*";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Content-Type,X-Requested-With";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "GET, PUT, POST, DELETE, HEAD, OPTIONS";

    private boolean accessControlAllow = true;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletResponse instanceof HttpServletResponse){
            logger.trace("XSS Headers ...");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            if (accessControlAllow) {
                String urlString = ACCESS_CONTROL_ALLOW_ORIGIN_DEFAULT;
                String testUrl = ((HttpServletRequest) servletRequest).getHeader(REQUEST_ORIGIN);
                if (testUrl != null && !StringUtils.isEmpty(testUrl)) {
                    urlString = testUrl;
                }
                response.addHeader("Access-Control-Allow-Origin", urlString);
                response.addHeader("Access-Control-Allow-Credentials", ACCESS_CONTROL_ALLOW_CREDENTIALS);
                response.addHeader("Access-Control-Allow-Headers", ACCESS_CONTROL_ALLOW_HEADERS);
                response.addHeader("Access-Control-Allow-Methods", ACCESS_CONTROL_ALLOW_METHODS);
            }
            response.addHeader("Cache-Control", "private");
            logger.trace("XSS Headers done.");
        }

        //follow the chain
        filterChain.doFilter(servletRequest,servletResponse);
    }

    public boolean isAccessControlAllow() {
        return accessControlAllow;
    }

    public void setAccessControlAllow(boolean accessControlAllow) {
        this.accessControlAllow = accessControlAllow;
    }
}

