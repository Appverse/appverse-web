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
package org.appverse.web.framework.frontend.gwt.commands;

import com.mvp4g.client.event.BaseEventHandler;
import com.mvp4g.client.event.EventBusWithLookup;
import org.appverse.web.framework.frontend.gwt.common.utils.GWTUtils;

public abstract class AbstractCommand<E extends EventBusWithLookup> extends
		BaseEventHandler<E> {

    /**
     * Check hash (#) in URL to detect place event on module loading. If
     * detected, dispatch eventBus to that event. This should be used with
     * eventBus historyOnStart = false
     *
     */
    protected void checkInitPlaceEvent() {
        String initPlaceEvent = GWTUtils.checkInitPlaceEvent();
        if (initPlaceEvent != null && initPlaceEvent.trim().length() > 0) {
            eventBus.dispatch(initPlaceEvent);
        }
    }

    /**
     * Check hash (#) in URL to detect place event on module loading. If
     * detected, the URL is returned. This should be used with
     * eventBus historyOnStart = false
     *
     */
    protected String getInitPlaceEvent() {
        return GWTUtils.checkInitPlaceEvent();
    }
}
