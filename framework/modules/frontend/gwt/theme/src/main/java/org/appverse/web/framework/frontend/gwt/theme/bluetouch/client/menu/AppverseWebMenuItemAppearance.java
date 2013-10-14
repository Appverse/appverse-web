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
import com.sencha.gxt.theme.base.client.menu.MenuItemBaseAppearance;

public class AppverseWebMenuItemAppearance extends MenuItemBaseAppearance {

  public static class AppverseWebMenuItemAppearanceHelper {

    public static String getMenuParent() {
      return new StringBuilder("url(").append(GWT.getModuleBaseURL()).append("bluetouch/images/menu/menuParent.gif);").toString();
    }

  }

  public interface AppverseWebMenuItemResources extends MenuItemBaseAppearance.MenuItemResources, ClientBundle {

    ImageResource menuParent();

    @Source({"com/sencha/gxt/theme/base/client/menu/MenuItem.css", "AppverseWebMenuItem.css"})
    AppverseWebMenuItemStyle style();

  }

  public interface AppverseWebMenuItemStyle extends MenuItemBaseAppearance.MenuItemStyle {
  }

  public AppverseWebMenuItemAppearance() {
    this(GWT.<AppverseWebMenuItemResources> create(AppverseWebMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public AppverseWebMenuItemAppearance(AppverseWebMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
