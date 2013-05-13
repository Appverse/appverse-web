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
import com.sencha.gxt.theme.base.client.menu.CheckMenuItemBaseAppearance;
import com.sencha.gxt.theme.blue.client.menu.BlueMenuItemAppearance.BlueMenuItemResources;

public class DefaultCheckMenuItemAppearance extends CheckMenuItemBaseAppearance {

  public interface BlueCheckMenuItemResources extends CheckMenuItemResources, BlueMenuItemResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/menu/CheckMenuItem.css", "BlueCheckMenuItem.css"})
    BlueCheckMenuItemStyle checkStyle();

  }

  public interface BlueCheckMenuItemStyle extends CheckMenuItemStyle {
  }

  public DefaultCheckMenuItemAppearance() {
    this(GWT.<BlueCheckMenuItemResources> create(BlueCheckMenuItemResources.class),
        GWT.<MenuItemTemplate> create(MenuItemTemplate.class));
  }

  public DefaultCheckMenuItemAppearance(BlueCheckMenuItemResources resources, MenuItemTemplate template) {
    super(resources, template);
  }

}
