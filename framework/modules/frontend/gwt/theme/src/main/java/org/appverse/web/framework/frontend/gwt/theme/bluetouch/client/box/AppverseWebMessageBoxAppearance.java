/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.box;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.window.AppverseWebWindowAppearance;

public class AppverseWebMessageBoxAppearance extends AppverseWebWindowAppearance {

  public interface AppverseWebMessageBoxResources extends AppverseWebWindowResources, ClientBundle {

    @Source({
        "com/sencha/gxt/theme/base/client/panel/ContentPanel.css",
        "org/appverse/web/framework/frontend/gwt/theme/bluetouch/client/window/AppverseWebWindow.css"})
    @Override
    AppverseWebWindowStyle style();

  }

  public AppverseWebMessageBoxAppearance() {
    super((AppverseWebMessageBoxResources) GWT.create(AppverseWebMessageBoxResources.class));
  }
}
