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

import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
 */
public class StaticContentCacheFilter extends OncePerRequestFilter {

    @AutowiredLogger
    private static Logger logger;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;
        String requestUri = httpRequest.getRequestURI();
        if (!requestUri.contains(".nocache.")) {
            // Setting "Expires" +1 year following Google recommendations for static content
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            Date date = calendar.getTime();
            SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            httpResponse.addHeader("Expires", df.format(date));
            logger.debug("Added Expires:" + df.format(date)  + " header to request. Request URI: " + requestUri);
        } else {
            // We do not cache non-cacheable GWT resources
            httpResponse.addHeader("Cache-Control", "no-cache");
            logger.debug("Added Cache-Control:no-cache header to request. Request URI: " + requestUri);
        }
        filterChain.doFilter(req, res);
    }
}