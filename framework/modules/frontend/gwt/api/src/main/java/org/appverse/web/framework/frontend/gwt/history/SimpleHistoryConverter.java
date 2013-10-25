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
import org.appverse.web.framework.frontend.gwt.common.FrameworkEventBus;

/**
 * This history converter it is useful when you follow a strategy that allows only navigation between "pages" (places)
 * in your application but you do NOT restore the status of the application (the status is keept in the view itself).
 * This is the simpler strategy, the one that requires less code and allows to reuse more boilerplate code.
 * The tokens does not have parameters, just the name of the token / event to dispatch. Take into account that this simple history converter is NOT
 * designed to restore the status of the application to a certain point using parameters, as this converter does not allows parameters.
 * This will work with events that does not require any parameter, instead.
 */
@History(type = History.HistoryConverterType.SIMPLE)
public class SimpleHistoryConverter extends AbstractHistoryConverter<FrameworkEventBus> {

    public String convertToToken(final String name) {
        return null;
    }

    @Override
    public void convertFromToken(String name, String param, FrameworkEventBus eventBus) {
        eventBus.dispatch(name);
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}