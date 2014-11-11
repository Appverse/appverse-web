/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.theme.base.client.toolbar.ToolBarBaseAppearance;

public class AppverseWebToolBarAppearance extends ToolBarBaseAppearance {

  public interface AppverseWebToolBarResources extends ClientBundle {
    @Source({"com/sencha/gxt/theme/base/client/toolbar/ToolBarBase.css", "AppverseWebToolBar.css"})
    AppverseWebToolBarStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource background();

  }

  public interface AppverseWebToolBarStyle extends ToolBarBaseStyle, CssResource {

  }

  private final AppverseWebToolBarStyle style;
  private final AppverseWebToolBarResources resources;

  public AppverseWebToolBarAppearance() {
    this(GWT.<AppverseWebToolBarResources> create(AppverseWebToolBarResources.class));
  }

  public AppverseWebToolBarAppearance(AppverseWebToolBarResources resources) {
    this.resources = resources;
    this.style = this.resources.style();
   
    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public String toolBarClassName() {
    return style.toolBar();
  }

}
