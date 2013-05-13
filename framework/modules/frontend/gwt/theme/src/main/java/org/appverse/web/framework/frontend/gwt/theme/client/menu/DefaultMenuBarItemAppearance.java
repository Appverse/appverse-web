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
import com.sencha.gxt.theme.base.client.menu.MenuBarItemBaseAppearance;

public class DefaultMenuBarItemAppearance extends MenuBarItemBaseAppearance {

  public interface BlueMenuBarItemResources extends MenuBarItemResources, ClientBundle {
    @Source({"com/sencha/gxt/theme/base/client/menu/MenuBarItem.css", "BlueMenuBarItem.css"})
    BlueMenuBarItemStyle css();
  }
  
  public interface BlueMenuBarItemStyle extends MenuBarItemStyle {
  }
  
  public DefaultMenuBarItemAppearance() {
    this(GWT.<BlueMenuBarItemResources>create(BlueMenuBarItemResources.class));
  }
  
  public DefaultMenuBarItemAppearance(BlueMenuBarItemResources resources) {
    super(resources);
  }

}
