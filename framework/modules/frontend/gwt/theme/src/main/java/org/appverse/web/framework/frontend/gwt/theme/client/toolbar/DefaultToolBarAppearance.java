/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.theme.base.client.toolbar.ToolBarBaseAppearance;

public class DefaultToolBarAppearance extends ToolBarBaseAppearance {

  public interface BlueToolBarResources extends ClientBundle {
    @Source({"com/sencha/gxt/theme/base/client/toolbar/ToolBarBase.css", "BlueToolBar.css"})
    BlueToolBarStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource background();

  }

  public interface BlueToolBarStyle extends ToolBarBaseStyle, CssResource {

  }

  private final BlueToolBarStyle style;
  private final BlueToolBarResources resources;

  public DefaultToolBarAppearance() {
    this(GWT.<BlueToolBarResources> create(BlueToolBarResources.class));
  }

  public DefaultToolBarAppearance(BlueToolBarResources resources) {
    this.resources = resources;
    this.style = this.resources.style();
   
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public String toolBarClassName() {
    return style.toolBar();
  }

}
