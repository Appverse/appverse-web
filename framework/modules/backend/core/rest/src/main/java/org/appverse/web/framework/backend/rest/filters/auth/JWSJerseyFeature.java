package org.appverse.web.framework.backend.rest.filters.auth;/*
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;

/**
 * Feature of JWSJerseyFilter Obtains a key and configures it within the client filter
 *
 *
 * @See JWSJerseyFilter
 */
public class JWSJerseyFeature implements Feature{
    private static Logger logger = LoggerFactory.getLogger(JWSJerseyFeature.class);

    private final Key key;

    public JWSJerseyFeature(Key key) {
       this.key = key;

    }
    public JWSJerseyFeature(KeyStore keyStore, String alias, String password) {

        try {
            this.key = keyStore.getKey(alias, password.toCharArray());
        }catch (GeneralSecurityException  e){
            logger.error("invadid keystore ",e);
            throw new RuntimeException("invadid keystore", e);
        }

    }

    @Override
    public boolean configure(final FeatureContext context) {
        final Configuration config = context.getConfiguration();
        //client side only configuration
        if (RuntimeType.CLIENT == config.getRuntimeType()) {
            if (!config.isRegistered(JWSJerseyFilter.class)){
                context.property(JWSJerseyFilter.JWS_FILTER_KEY, key);
                context.register(JWSJerseyFilter.class);
                return true;
            }
        }
        return false;
    }
    public static Builder basicBuilder() {
        return new BuilderImpl();
    }
    public static interface Builder {

        /**
         * Set credentials.
         *
         * @param key client certificate.
         * @return This builder.
         */
        public Builder credentials(Key key);


        /**
         * Build the feature.
         *
         * @return Http authentication feature configured from this builder.
         */
        public JWSJerseyFeature build();
    }
    static class BuilderImpl implements Builder {
        private Key key;

        public Builder credentials(Key key){
            this.key = key;
            return this;
        }
        public JWSJerseyFeature build(){
            return new JWSJerseyFeature(key);
        }
    }

}
