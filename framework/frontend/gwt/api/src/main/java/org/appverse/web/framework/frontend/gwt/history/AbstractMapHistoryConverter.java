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
import com.mvp4g.client.event.EventBus;

import java.util.HashMap;

/**
 * Abstract history converter designed to deal with parameters tokenQueryStrings based on 'key' and 'value' formats (not positional).
 * Example: param1id=value1&param2id=value2
 * @param <E>
 */
@History(type = History.HistoryConverterType.DEFAULT)
public abstract class AbstractMapHistoryConverter<E extends EventBus> extends AbstractHistoryConverter<E> {

    @Override
	public void convertFromToken(final String name, final String tokenQueryString, final E eventBus){
        HashMap<String, String> params = null;
        if (tokenQueryString != null){
            params = getParams(tokenQueryString);
        }
        convertFromToken(name, params, eventBus);
    }

    protected HashMap getParams(String queryString){
        HashMap<String, String> params = new HashMap<String, String>();
        for (String param : queryString.split(QUERY_STRING_PARAM_SEPARATOR)) {
            String[] pair = param.split("=");
            String key = pair[0];
            String value = getNullOrValue(pair[1]);
            params.put(key, value);
        }
        return params;
    }

    /**
     * Method to be implemented by extending classes. It receives the parsed params in a HashMap
     * @param name Token name
     * @param params Hashmap containing the parameters. The key is the parameter name and the value contains the param value as String
     * @param eventBus
     */
    protected abstract void convertFromToken(final String name, final HashMap<String, String> params,
                                 final E eventBus);

}