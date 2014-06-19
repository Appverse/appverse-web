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
import com.sencha.gxt.theme.base.client.menu.CheckMenuItemBaseAppearance;
import org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.menu.AppverseWebMenuItemAppearance.AppverseWebMenuItemResources;

public class AppverseWebCheckMenuItemAppearance extends CheckMenuItemBaseAppearance {

  public interface AppverseWebCheckMenuItemResources extends CheckMenuItemResources, AppverseWebMenuItemResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/menu/CheckMenuItem.css", "AppverseWebCheckMenuItem.css"})
    AppverseWebCheckMenuItemStyle checkStyle();

  }

  public interface AppverseWebCheckMenuItemStyle extends CheckMenuItemStyle {
  }

  public AppverseWebCheckMenuItemAppearance() {
    this(GWT.<AppverseWebCheckMenuItemResources> create(AppverseWebCheckMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public AppverseWebCheckMenuItemAppearance(AppverseWebCheckMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
