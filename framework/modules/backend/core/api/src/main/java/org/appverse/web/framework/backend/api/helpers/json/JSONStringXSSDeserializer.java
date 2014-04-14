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
package org.appverse.web.framework.backend.api.helpers.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.reference.DefaultEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * This class is used by Appverse JacksonContextResolver to deserialize JSON String values.
 */
public class JSONStringXSSDeserializer extends JsonDeserializer<String> {
    private static Logger logger = LoggerFactory.getLogger(JSONStringXSSDeserializer.class);

    @Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

        String rawValue = jp.getText();
        //String encValue = stripXSS(rawValue);
        String encValue = encodeHtml(rawValue);
        //System.out.println("JSONStringXSSDeserializer 1["+rawValue+"] to ["+encValue+"]");
		return encValue;
	}

    /**
     * Using ESAPI HTML Encoder, encodes the supplied html string.
     * @param html the string to be encoded.
     * @return the encoded string.
     */
    private String encodeHtml(String html) {
        Encoder encoder = DefaultEncoder.getInstance();
        String s = encoder.encodeForHTML(html);
        //System.out.println("Encoded from ["+html+"] to ["+s+"]");
        return s;
    }

    /**
     * This method removes all html markup from the supplied string.
     * @param value The string containing possible html tags.
     * @return The string without html tags.
     */
    private String stripXSS( String value )
    {
        if( value != null )
        {
          //  System.out.println("STRIP XSS -> ["+value+"]");
            // Use the ESAPI library to avoid encoded attacks.
            value = ESAPI.encoder().canonicalize( value );
            //ESAPI.encoder().encodeForHTML()

            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Clean out HTML
            //This clean, removes all html tags. so instead of &lt;script&gt;, it simple removes the <script> tag.
            value = Jsoup.clean(value, Whitelist.none());
            //System.out.println("STRIPED XSS -> ["+value+"]");
        }
        return value;
    }
}
