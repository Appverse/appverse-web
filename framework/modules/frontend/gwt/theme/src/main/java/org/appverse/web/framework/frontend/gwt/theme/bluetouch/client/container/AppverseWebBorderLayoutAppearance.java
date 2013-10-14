/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.container;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.container.BorderLayoutBaseAppearance;

public class AppverseWebBorderLayoutAppearance extends BorderLayoutBaseAppearance {

  public interface AppverseWebBorderLayoutResources extends BorderLayoutResources {
    @Override
    @Source({"com/sencha/gxt/theme/base/client/container/BorderLayout.css", "AppverseWebBorderLayout.css"})
    public AppverseWebBorderLayoutStyle css();
  }

  public interface AppverseWebBorderLayoutStyle extends BorderLayoutStyle {

  }

  public AppverseWebBorderLayoutAppearance() {
    super(GWT.<AppverseWebBorderLayoutResources> create(AppverseWebBorderLayoutResources.class));
  }

}
