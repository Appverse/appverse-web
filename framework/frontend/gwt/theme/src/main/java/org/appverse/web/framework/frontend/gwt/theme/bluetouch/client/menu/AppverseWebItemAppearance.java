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
import com.sencha.gxt.theme.base.client.menu.ItemBaseAppearance;

public class AppverseWebItemAppearance extends ItemBaseAppearance {

  public interface AppverseWebItemResources extends ItemBaseAppearance.ItemResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/menu/Item.css", "AppverseWebItem.css"})
    AppverseWebItemStyle style();

    ImageResource itemOver();

  }

  public interface AppverseWebItemStyle extends ItemStyle {

    String active();

  }

  public AppverseWebItemAppearance() {
    this(GWT.<AppverseWebItemResources> create(AppverseWebItemResources.class));
  }

  public AppverseWebItemAppearance(AppverseWebItemResources resources) {
    super(resources);
  }

}
