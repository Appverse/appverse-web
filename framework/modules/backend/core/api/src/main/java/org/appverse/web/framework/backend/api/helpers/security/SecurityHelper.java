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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecurityHelper {

    public static final String XSRF_TOKEN_NAME = "XSRF-TOKEN";

    @SuppressWarnings("unchecked")
    public static List<String> getAuthorities() {
        final Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        List<String> credentials = new ArrayList<String>();
        Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) authentication
                .getAuthorities();
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            credentials.add(grantedAuthority.getAuthority());
        }
        return credentials;
    }

    public static String getPrincipal() {
        final Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        return authentication.getName();
    }


    public static String createXSRFToken(final HttpServletRequest request)
            throws IOException {
        // getSession(false) as this method never creates a new session
        HttpSession session = request.getSession(false);
        String xsrfSessionToken = (String) session
                .getAttribute(XSRF_TOKEN_NAME);
        if (xsrfSessionToken == null) {
            Random r = new Random(System.currentTimeMillis());
            long value = System.currentTimeMillis() + r.nextLong();
            char ids[] = session.getId().toCharArray();
            for (int i = 0; i < ids.length; i++) {
                value += ids[i] * (i + 1);
            }
            xsrfSessionToken = Long.toString(value);
            session.setAttribute(XSRF_TOKEN_NAME, xsrfSessionToken);
        }
        return xsrfSessionToken;
    }

    /**
     *
     * @param request
     * @throws Exception
     */
    @SuppressWarnings("unused")
    public static void checkXSRFToken(final HttpServletRequest request)
            throws Exception {
        String tokenFromRequest = request.getHeader(XSRF_TOKEN_NAME);
        checkXSRFToken(tokenFromRequest, request);
    }

    public static void checkXSRFToken(final String xsrfToken, final HttpServletRequest request)
            throws Exception {
        String sessionValue = (String) request.getSession().getAttribute(SecurityHelper.XSRF_TOKEN_NAME);
        if (xsrfToken == null || sessionValue == null || !sessionValue.equals(xsrfToken)) {
            throw new Exception("XSRF attribute not found in request or session or invalid.");
        }
    }
}