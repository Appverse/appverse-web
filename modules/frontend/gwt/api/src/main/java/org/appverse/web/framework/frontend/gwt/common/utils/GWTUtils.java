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
