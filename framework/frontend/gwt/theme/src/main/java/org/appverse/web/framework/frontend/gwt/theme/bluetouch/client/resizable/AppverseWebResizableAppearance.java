/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.resizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.resizable.ResizableBaseAppearance;

public class AppverseWebResizableAppearance extends ResizableBaseAppearance {

  public interface AppverseWebResizableResources extends ResizableResources, ClientBundle {

    ImageResource handleEast();

    ImageResource handleNortheast();

    ImageResource handleNorthwest();

    ImageResource handleSouth();

    ImageResource handleSoutheast();

    ImageResource handleSouthwest();

    @Source({"com/sencha/gxt/theme/base/client/resizable/Resizable.css", "AppverseWebResizable.css"})
    AppverseWebResizableStyle style();

  }

  public interface AppverseWebResizableStyle extends ResizableStyle {
  }

  public AppverseWebResizableAppearance() {
    this(GWT.<AppverseWebResizableResources> create(AppverseWebResizableResources.class));
  }

  public AppverseWebResizableAppearance(ResizableResources resources) {
    super(resources);
  }

}
