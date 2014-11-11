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
import com.sencha.gxt.theme.base.client.menu.MenuBarItemBaseAppearance;

public class AppverseWebMenuBarItemAppearance extends MenuBarItemBaseAppearance {

  public interface AppverseWebMenuBarItemResources extends MenuBarItemResources, ClientBundle {
    @Source({"com/sencha/gxt/theme/base/client/menu/MenuBarItem.css", "AppverseWebMenuBarItem.css"})
    AppverseWebMenuBarItemStyle css();
  }
  
  public interface AppverseWebMenuBarItemStyle extends MenuBarItemStyle {
  }
  
  public AppverseWebMenuBarItemAppearance() {
    this(GWT.<AppverseWebMenuBarItemResources>create(AppverseWebMenuBarItemResources.class));
  }
  
  public AppverseWebMenuBarItemAppearance(AppverseWebMenuBarItemResources resources) {
    super(resources);
  }

}
