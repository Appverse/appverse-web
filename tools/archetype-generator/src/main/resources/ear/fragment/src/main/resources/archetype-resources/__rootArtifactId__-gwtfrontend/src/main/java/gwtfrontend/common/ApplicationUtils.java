package ${package}.gwtfrontend.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class ApplicationUtils {

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

	private ApplicationUtils() {
		// Default constructor made private to avoid class instantiation
	}
}
