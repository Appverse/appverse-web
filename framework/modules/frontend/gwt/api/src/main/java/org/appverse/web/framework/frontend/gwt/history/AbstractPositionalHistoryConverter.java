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
package org.appverse.web.framework.frontend.gwt.history;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.event.EventBus;

/**
 * Abstract history converter designed to deal with parameters tokenQueryStrings based on parameters positon.
 * Example: param1value&param2value
 * @param <E>
 */
@History(type = HistoryConverterType.DEFAULT)
public abstract class AbstractPositionalHistoryConverter<E extends EventBus> extends AbstractHistoryConverter<E> {

    /**
     * Helper mehtod (optional) that provides an automatic way to convert the string representation of the parameters to a formatted token
     * @param parameters String representation of the parameters
     * @return formatted token
     */
    protected String automaticConvertToToken(final String... parameters) {
        String token;
        final StringBuilder tokenBuilder = new StringBuilder();

        if (parameters != null && parameters.length > 0) {
            tokenBuilder.append(QUERY_STRING_PARAM_SEPARATOR);

            for (int i = 0; i < parameters.length; i++) {
                final String parameter = parameters[i];

                tokenBuilder.append(getNilIfNull(parameter));
                if (i < parameters.length - 1) {
                    tokenBuilder.append(QUERY_STRING_PARAM_SEPARATOR);
                }
            }
        }
        token = tokenBuilder.toString();
        return token;
    }


    @Override
    public void convertFromToken(final String name, final String tokenQueryString, final E eventBus) {
        if (tokenQueryString != null) {
            final String[] parameters = tokenQueryString.split(QUERY_STRING_PARAM_SEPARATOR);
            convertFromToken(name, parameters, eventBus);
        } else {
            convertFromToken(name, new String[] {}, eventBus);
        }
    }


    /**
     * Method to be implemented by extending classes. It receives the parsed params in order
     * @param name Token name
     * @param params String array containing the parameters ordered by position
     * @param eventBus
     */
    protected abstract void convertFromToken(final String name, final String[] params,
                                 final E eventBus);

}