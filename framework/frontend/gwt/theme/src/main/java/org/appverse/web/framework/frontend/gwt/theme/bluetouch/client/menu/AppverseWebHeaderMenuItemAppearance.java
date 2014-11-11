/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Element;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem.HeaderMenuItemAppearance;

public class AppverseWebHeaderMenuItemAppearance extends AppverseWebItemAppearance implements HeaderMenuItemAppearance {

  public interface AppverseWebHeaderMenuItemResources extends AppverseWebItemResources {

    @Source("AppverseWebHeaderMenuItem.css")
    AppverseWebHeaderMenuItemStyle headerStyle();

  }

  public interface AppverseWebHeaderMenuItemStyle extends CssResource {

    public String menuText();

  }

  private AppverseWebHeaderMenuItemStyle headerStyle;

  public AppverseWebHeaderMenuItemAppearance() {
    this(GWT.<AppverseWebHeaderMenuItemResources> create(AppverseWebHeaderMenuItemResources.class));
  }

  public AppverseWebHeaderMenuItemAppearance(AppverseWebHeaderMenuItemResources resources) {
    super(resources);
    headerStyle = resources.headerStyle();
    headerStyle.ensureInjected();
  }

  @Override
  public void applyItemStyle(Element element) {
    element.addClassName(headerStyle.menuText());
  }

}
