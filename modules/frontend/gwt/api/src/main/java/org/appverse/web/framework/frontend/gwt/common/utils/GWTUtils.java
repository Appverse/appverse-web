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
package org.appverse.web.framework.frontend.gwt.common.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;

public class GWTUtils {
	
	public static final String DEFAULT_HISTORY_TOKEN = "#";

	public static String getModuleIndependentBaseURL() {
		String moduleBaseUrl = GWT.getModuleBaseURL();
		String moduleName = GWT.getModuleName() + "/";
		return moduleBaseUrl.replace(moduleName, "");
	}

	public static String getUserAgent() {
		return Window.Navigator.getUserAgent();
	}

	public static boolean isUserAgentChrome() {
		return getUserAgent().contains("Chrome");
	}

	public static boolean isUserAgentFirefox() {
		return getUserAgent().contains("Firefox");
	}

	public static boolean isUserAgentSafari() {
		return !isUserAgentChrome() && getUserAgent().contains("Safari");
	}

	public static void goToModuleUrl(final String moduleStartHtml,
			final String placeEventName) {
		UrlBuilder builder = Window.Location.createUrlBuilder();
		builder.setPath(moduleStartHtml);
		builder.setHash(placeEventName);
		Window.Location.replace(builder.buildString());
	}

	/**
	 * Check hash (#) in URL to detect place event on module loading.
	 * This should be used with eventBus historyOnStart = false
	 */	
	public static String checkInitPlaceEvent() {
		String hash = Window.Location.getHash();
		if (hash != null && hash.trim().length() > 0) {
			int pos = hash.indexOf(DEFAULT_HISTORY_TOKEN);
			if (pos != -1)
				hash = hash.substring(pos + 1);
			return hash;
		}
		return null;
	}

	private GWTUtils() {
		// Default constructor made private to avoid class instantiation
	}
}
