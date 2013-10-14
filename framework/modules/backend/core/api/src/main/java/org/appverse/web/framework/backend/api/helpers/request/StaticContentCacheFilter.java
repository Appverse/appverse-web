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
package org.appverse.web.framework.backend.api.helpers.request;

import jodd.typeconverter.Convert;
import jodd.typeconverter.TypeConversionException;
import jodd.util.Wildcard;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This filter sets the proper http header for those filtered resources we want to cache. It implements "strong caching" (one year).
 * Do not cache resources that change often. For semi-static resources a strategy based on heuristic or url modifying is recommended
 * (you can use "strong caching" for resources that are application data if you are sure your application modifies the URL every time the data is modified).
 * GWT apps resources marked with standard ".nocache." will be bypassed.
 * This solution is based on Google recommendations: https://developers.google.com/speed/docs/best-practices/caching?hl=es
 *
 * Params initialization and url patterns treatment (matches, nocaches and wildcards) is inspired by jodd.servlet.filter.GzipFilter.java
 * http://jodd.org/doc/htmlstapler/enabling-gzip.html#GZIP-filter
 */
public class StaticContentCacheFilter implements Filter {

    protected String[] matches;
    protected String[] nocaches;
    protected boolean wildcards;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        if (isNoCacheable(httpRequest) == true){
            // We do not cache non-cacheable GWT resources
            httpResponse.addHeader("Cache-Control", "no-cache");
        }else if (isCacheableEligible(httpRequest) == true) {
            // Is cacheable
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            Date date = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            httpResponse.addHeader("Expires", df.format(date));
        }
        else if (isCacheableEligible(httpRequest) == false) {
            // We do not mofidy the response
        }
        chain.doFilter(httpRequest, httpResponse);
    }

    /**
     * Filter initialization.
     */
    @Override
    public void init(FilterConfig config) throws ServletException {

        try {
            wildcards = Convert.toBooleanValue(config.getInitParameter("wildcards"), false);
        } catch (TypeConversionException ignore) {
            wildcards = false;
        }

        // match string
        String uriMatch = config.getInitParameter("match");

        if ((uriMatch != null) && (uriMatch.equals("*") == false)) {
            matches = uriMatch.split(",");
            for (int i = 0; i < matches.length; i++) {
                matches[i] = matches[i].trim();
            }
        }

        // exclude string
        String uriNoCache = config.getInitParameter("nocache");

        if (uriNoCache != null) {
            nocaches = uriNoCache.split(",");
            for (int i = 0; i < nocaches.length; i++) {
                nocaches[i] = nocaches[i].trim();
            }
        }
    }

    public void destroy() {
        matches = null;
        nocaches = null;
    }

    /**
     * Determine if uri is eligible for caching
     */
    private boolean isCacheableEligible(HttpServletRequest req) {
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

        if ((result == true) && (nocaches != null)) {
            if (wildcards) {
                if (Wildcard.matchPathOne(uri, nocaches) != -1) {
                    result = false;
                }
            } else {
                for (String nocache : nocaches) {
                    if (uri.contains(nocache)) {
                        result = false;						// nocaches found
                        break;
                    }
                }
            }
        }
        return result;
    }

    private boolean isNoCacheable(HttpServletRequest req){
        String uri = req.getRequestURI();
        if (uri == null) {
            return false;
        }

        boolean result = false;

        if (nocaches != null) {
            if (wildcards) {
                if (Wildcard.matchPathOne(uri, nocaches) != -1) {
                    result = true;
                }
            } else {
                for (String nocache : nocaches) {
                    if (uri.contains(nocache)) {
                        result = true;						// nocaches found
                        break;
                    }
                }
            }
        }
        return result;
    }

}