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
import com.sencha.gxt.theme.base.client.menu.SeparatorMenuItemBaseAppearance;

public class AppverseWebSeparatorMenuItemAppearance extends SeparatorMenuItemBaseAppearance {

  public interface AppverseWebSeparatorMenuItemResources extends ClientBundle, SeparatorMenuItemResources {

    @Source({"com/sencha/gxt/theme/base/client/menu/SeparatorMenuItem.css", "AppverseWebSeparatorMenuItem.css"})
    AppverseWebSeparatorMenuItemStyle style();

  }

  public interface AppverseWebSeparatorMenuItemStyle extends SeparatorMenuItemStyle {
  }

  public AppverseWebSeparatorMenuItemAppearance() {
    this(GWT.<AppverseWebSeparatorMenuItemResources> create(AppverseWebSeparatorMenuItemResources.class),
        GWT.<SeparatorMenuItemTemplate> create(SeparatorMenuItemTemplate.class));
  }

  public AppverseWebSeparatorMenuItemAppearance(AppverseWebSeparatorMenuItemResources resources,
      SeparatorMenuItemTemplate template) {
    super(resources, template);
  }

}
