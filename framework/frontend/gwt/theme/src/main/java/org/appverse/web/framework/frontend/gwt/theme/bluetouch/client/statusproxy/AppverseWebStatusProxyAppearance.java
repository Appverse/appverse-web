/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.statusproxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.statusproxy.StatusProxyBaseAppearance;

public class AppverseWebStatusProxyAppearance extends StatusProxyBaseAppearance {

  public interface AppverseWebStatusProxyResources extends StatusProxyResources, ClientBundle {

    ImageResource dropAllowed();

    ImageResource dropDisallowed();

    @Source({"com/sencha/gxt/theme/base/client/statusproxy/StatusProxy.css", "AppverseWebStatusProxy.css"})
    AppverseWebStatusProxyStyle style();

  }

  public interface AppverseWebStatusProxyStyle extends StatusProxyStyle {
  }

  public AppverseWebStatusProxyAppearance() {
    this(GWT.<AppverseWebStatusProxyResources> create(AppverseWebStatusProxyResources.class),
        GWT.<StatusProxyTemplates> create(StatusProxyTemplates.class));
  }

  public AppverseWebStatusProxyAppearance(AppverseWebStatusProxyResources resources, StatusProxyTemplates templates) {
    super(resources, templates);
  }

}
