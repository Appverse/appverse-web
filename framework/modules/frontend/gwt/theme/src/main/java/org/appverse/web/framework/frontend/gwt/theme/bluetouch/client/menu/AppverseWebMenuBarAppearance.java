/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.menu.MenuBarBaseAppearance;

public class AppverseWebMenuBarAppearance extends MenuBarBaseAppearance {

  public interface AppverseWebMenuBarResources extends MenuBarResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/menu/MenuBar.css", "AppverseWebMenuBar.css"})
    AppverseWebMenuBarStyle css();

    @ImageOptions(repeatStyle=RepeatStyle.Horizontal)
    ImageResource background();

  }

  public interface AppverseWebMenuBarStyle extends MenuBarStyle {
  }

  public AppverseWebMenuBarAppearance() {
    this(GWT.<AppverseWebMenuBarResources> create(AppverseWebMenuBarResources.class));
  }

  public AppverseWebMenuBarAppearance(AppverseWebMenuBarResources resources) {
    super(resources);
  }

}
