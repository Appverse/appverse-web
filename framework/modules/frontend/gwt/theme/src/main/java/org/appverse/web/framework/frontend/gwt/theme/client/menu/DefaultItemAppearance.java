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
import com.sencha.gxt.theme.base.client.menu.ItemBaseAppearance;

public class DefaultItemAppearance extends ItemBaseAppearance {

  public interface BlueItemResources extends ItemBaseAppearance.ItemResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/menu/Item.css", "BlueItem.css"})
    BlueItemStyle style();

    ImageResource itemOver();

  }

  public interface BlueItemStyle extends ItemStyle {

    String active();

  }

  public DefaultItemAppearance() {
    this(GWT.<BlueItemResources> create(BlueItemResources.class));
  }

  public DefaultItemAppearance(BlueItemResources resources) {
    super(resources);
  }

}
