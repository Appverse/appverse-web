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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.reference.DefaultEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESAPIHelper {
	
	private static Logger logger = LoggerFactory.getLogger(ESAPIHelper.class);
	
    /**
     * Using ESAPI HTML Encoder, encodes the supplied html string.
     * @param html the string to be encoded.
     * @return the encoded string.
     */
    public static String encodeHtml(String html) {    	
        return DefaultEncoder.getInstance().encodeForHTML(html);
    }
	
    /**
     * Apply the XSS filter to the parameters in a MultivaluedMap
     * @param parameters
     */
    public static void cleanParams( MultivaluedMap<String, String> parameters )
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
     * Apply the XSS filter to the parameters in a Map
     * @param parameters
     */
    public static void cleanParams( Map<String, String> parameters )
    {
        logger.debug("Checking for XSS Vulnerabilities: {}", parameters);

        for( Map.Entry<String, String> params : parameters.entrySet() )
        {
            String key = params.getKey();
            String value = params.getValue();
            parameters.put( key, stripXSS( value ));
        }

        logger.debug( "XSS Vulnerabilities removed: {}", parameters );
    }

    /**
     * Strips any potential XSS threats out of the value
     * @param value
     * @return
     */
    public static String stripXSS( String value )
    {
        if( value != null )
        {
            // Use the ESAPI library to avoid encoded attacks.
            value = ESAPI.encoder().canonicalize( value );

            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Clean out HTML
            // This clean, removes all html tags. so instead of &lt;script&gt;, it simple removes the <script> tag.
            value = Jsoup.clean(value, Whitelist.none());
        }
        return value;
    }
}