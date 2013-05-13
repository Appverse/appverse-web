/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.menu.MenuItemBaseAppearance;

public class DefaultMenuItemAppearance extends MenuItemBaseAppearance {

  public static class BlueMenuItemAppearanceHelper {

    public static String getMenuParent() {
      return new StringBuilder("url(").append(GWT.getModuleBaseURL()).append("blue/images/menu/menuParent.gif);").toString();
    }

  }

  public interface BlueMenuItemResources extends MenuItemBaseAppearance.MenuItemResources, ClientBundle {

    ImageResource menuParent();

    @Source({"com/sencha/gxt/theme/base/client/menu/MenuItem.css", "BlueMenuItem.css"})
    BlueMenuItemStyle style();

  }

  public interface BlueMenuItemStyle extends MenuItemBaseAppearance.MenuItemStyle {
  }

  public DefaultMenuItemAppearance() {
    this(GWT.<BlueMenuItemResources> create(BlueMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public DefaultMenuItemAppearance(BlueMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
