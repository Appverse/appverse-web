package org.appverse.web.framework.frontend.gwt.entrypoint;

import com.mvp4g.client.Mvp4gEntryPoint;
import org.appverse.web.framework.frontend.gwt.helpers.dispatcher.AppverseDispatcher;
import org.fusesource.restygwt.client.Defaults;

/**
 * Initialize RestyGWT to match the servlet path configured in web.xml.
 */
public abstract class AppverseRestAbstractEntryPoint extends Mvp4gEntryPoint{
	
    @Override
    public void onModuleLoad() {
        // The path after getHostPageBaseURL() has to match what is defined in the web.xml for the JAX-RS Servlet
        Defaults.setServiceRoot(com.google.gwt.core.client.GWT.getHostPageBaseURL() + provideAppPath());
        // This is responsible for sending the XSRF header in all requests
        Defaults.setDispatcher(AppverseDispatcher.INSTANCE);

        super.onModuleLoad();
    }
    
    public abstract String provideAppPath();

    
}
