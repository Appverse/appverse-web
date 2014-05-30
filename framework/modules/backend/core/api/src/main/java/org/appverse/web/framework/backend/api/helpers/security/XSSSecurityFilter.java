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

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Request Filter to filter out possible XSS attacks in the request query parameters or headers.
 */

public class XSSSecurityFilter implements ContainerRequestFilter
{
    //private static final Logger LOG = LoggerFactory.getLogger(XSSSecurityFilter.class);
    //@AutowiredLogger
    private Logger logger = LoggerFactory.getLogger(XSSSecurityFilter.class);

    /**
     * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
     */
    @Override
    public void filter( ContainerRequestContext request )
    {
        // Clean the query strings
        cleanParams(request.getUriInfo().getQueryParameters());

        // Clean the headers
        cleanParams( request.getHeaders() );

        // Clean the cookies
        //cleanParams( request.getCookieNameValueMap() );

    }

    /**
     * Apply the XSS filter to the parameters
     * @param parameters
     */
    private void cleanParams( MultivaluedMap<String, String> parameters )
    {
        logger.debug("Checking for XSS Vulnerabilities: {}", parameters);

        for( Map.Entry<String, List<String>> params : parameters.entrySet() )
        {
            String key = params.getKey();
            List<String> values = params.getValue();

            List<String> cleanValues = new ArrayList<String>();
            for( String value : values )
            {
                cleanValues.add( stripXSS( value ) );
            }

            parameters.put( key, cleanValues );
        }

        logger.debug( "XSS Vulnerabilities removed: {}", parameters );
    }


    /**
     * Strips any potential XSS threats out of the value
     * @param value
     * @return
     */
    public String stripXSS( String value )
    {
        if( value != null )
        {
            // Use the ESAPI library to avoid encoded attacks.
            value = ESAPI.encoder().canonicalize( value );

            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Clean out HTML
            value = Jsoup.clean(value, Whitelist.none());
        }
        return value;
    }
}