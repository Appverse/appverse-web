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

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * JacksonContextResolver is a Custom Context Resolver that sets a default JSON String parser to filter out html tags.
 * This is in order to prevent XSS attacks.
 * In order to make use of this in your project, you just need to register as a Jersey Ressource.

 */
@Provider
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

    private static Logger logger = LoggerFactory.getLogger(JacksonContextResolver.class);

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        if( logger.isDebugEnabled()) {
            logger.debug("Using Appverse JacksonContextResolver.");
        }
        //
        //NICETOHAVE-> aClass is the Class for which Jackson is requesting a mapper.
        //using introspection/annotations, here we can decide if removing all html tags using JSONStringXSSDeserializer,
        // or use a more relaxed deserializer....
        //This way, by default all html code will be simply ignored, and only allowed when specific annotation is found.
                  /*
        Field[] fields = aClass.getDeclaredFields();
        for(Field field: fields) {
            JsonDeserialize t = field.getAnnotation(JsonDeserialize.class);
            if( t != null ) {
                System.out.println("Found field with JsonDeserialize annotation.");
            }
        }       */

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getJsonFactory();
        //allow single quotes in json format
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        mapper.registerModule(getCustomSimpleModuleWithXSS());

        return mapper;
    }

    public static SimpleModule getCustomSimpleModuleWithXSS() {
        //Define a SimpleModule to control Serialization mechanism.
        SimpleModule module = new SimpleModule("HTML XSS Serializer", new Version(1, 0, 0, "FINAL"));
        //module.addSerializer(String.class, new JSONHtmlXssSerializer());
        module.addDeserializer(String.class, new JSONStringXSSDeserializer());
        return module;
    }
}
