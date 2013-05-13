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
import com.sencha.gxt.theme.base.client.menu.MenuBaseAppearance;

public class DefaultMenuAppearance extends MenuBaseAppearance {

  public interface BlueMenuResources extends MenuBaseAppearance.MenuResources, ClientBundle {

    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.Vertical)
    ImageResource itemOver();

    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.Vertical)
    ImageResource menu();

    ImageResource miniBottom();

    ImageResource miniTop();

    @Source({"com/sencha/gxt/theme/base/client/menu/Menu.css", "BlueMenu.css"})
    BlueMenuStyle style();

  }

  public interface BlueMenuStyle extends MenuStyle {
  }

  public DefaultMenuAppearance() {
    this(GWT.<BlueMenuResources> create(BlueMenuResources.class), GWT.<BaseMenuTemplate> create(BaseMenuTemplate.class));
  }

  public DefaultMenuAppearance(BlueMenuResources resources, BaseMenuTemplate template) {
    super(resources, template);
  }

}
