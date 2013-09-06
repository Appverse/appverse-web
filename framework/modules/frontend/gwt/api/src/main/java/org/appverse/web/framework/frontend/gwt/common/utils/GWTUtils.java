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
import com.google.gwt.user.client.Window;

public class GWTUtils {
	
	public static final String DEFAULT_HISTORY_TOKEN = "#";

	public static String getModuleIndependentBaseURL() {
		String moduleBaseUrl = GWT.getModuleBaseURL();
		String moduleName = GWT.getModuleName() + "/";
        // We need to replace the last 'moduleName' in case context path is the same
        // or the string appears somewhere in the URL
        return moduleBaseUrl.replaceFirst(moduleName, "");
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

	/**
	 * This method allows to call other GWT modules passing the full URL and a
	 * initial 'place' to go in the application (the module called has to
	 * translate this initial 'place' to an event). This method keep the
	 * parameters in the original URL (current Window.Location) by default.
	 */
	public static void goToModuleUrl(final String urlToStartHtml,
			final String placeEventName) {
		goToModuleUrl(urlToStartHtml, placeEventName, true);
	}

	/**
	 * This method allows to call other GWT modules passing the full URL and a
	 * initial 'place' to go in the application (the module called has to
	 * translate this initial 'place' to an event). You can specify whether you
	 * want to keep the query string or not.
	 * 
	 */
	public static void goToModuleUrl(final String urlToStartHtml,
			final String placeEventName, boolean keepQueryString) {
		String fullUrl = urlToStartHtml;
		if (keepQueryString) {
			fullUrl += Window.Location.getQueryString();
		}
		if (placeEventName != null && !"".equals(placeEventName)) {
			fullUrl += "#" + placeEventName;
		}
		Window.Location.replace(fullUrl);
	}

	/**
	 * This method allows to call other GWT modules in the same application
	 * context path indicating the bootstrap module HTML page and a initial
	 * 'place' to go in the application (your module has to translate this
	 * initial 'place' to an event). This method keep the parameters in the
	 * original URL (current Window.Location) by default.
	 */
	public static void goToModuleUrlInSameApplicationContextPath(
			final String moduleStartHtml, final String placeEventName) {
		goToModuleUrlInSameApplicationContextPath(moduleStartHtml,
				placeEventName, true);
	}

	/**
	 * This method allows to call other GWT modules in the same application
	 * context path indicating the bootstrap module HTML page and a initial
	 * 'place' to go in the application (your module has to translate this
	 * initial 'place' to an event). This method keeps the parameters in the
	 * original URL (current Window.Location).
	 */
	public static void goToModuleUrlInSameApplicationContextPath(
			final String moduleStartHtml, final String placeEventName,
			boolean keepQueryString) {
		String fullUrl = getModuleIndependentBaseURL() + moduleStartHtml;
		if (keepQueryString) {
			fullUrl += Window.Location.getQueryString();
		}
		if (placeEventName != null && !"".equals(placeEventName)) {
			fullUrl += "#" + placeEventName;
		}
		Window.Location.replace(fullUrl);
	}

	/**
	 * Check hash (#) in URL to detect place event on module loading. This
	 * should be used with eventBus historyOnStart = false
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