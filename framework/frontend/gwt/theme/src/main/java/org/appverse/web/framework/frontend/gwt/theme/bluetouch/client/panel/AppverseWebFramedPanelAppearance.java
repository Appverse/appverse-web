/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame.NestedDivFrameStyle;
import com.sencha.gxt.theme.base.client.panel.FramedPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

public class AppverseWebFramedPanelAppearance extends FramedPanelBaseAppearance {

  public interface FramedPanelStyle extends ContentPanelStyle {

  }

  public interface AppverseWebFramePanelResources extends ContentPanelResources {
    @Source({"com/sencha/gxt/theme/base/client/panel/ContentPanel.css", "AppverseWebFramedPanel.css"})
    @Override
    FramedPanelStyle style();
  }

  public interface AppverseWebFramePanelNestedDivFrameStyle extends NestedDivFrameStyle {

  }

  public interface AppverseWebFramedPanelDivFrameResources extends FramedPanelDivFrameResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/frame/NestedDivFrame.css", "AppverseWebFramedPanelDivFrame.css"})
    @Override
    AppverseWebFramePanelNestedDivFrameStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Override
    ImageResource topLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource topBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource topRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource rightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource bottomBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomRightBorder();

  }

  public AppverseWebFramedPanelAppearance() {
    this(GWT.<AppverseWebFramePanelResources> create(AppverseWebFramePanelResources.class));
  }

  public AppverseWebFramedPanelAppearance(AppverseWebFramePanelResources resources) {
    super(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), new NestedDivFrame(
        GWT.<FramedPanelDivFrameResources> create(AppverseWebFramedPanelDivFrameResources.class)));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new AppverseWebHeaderFramedAppearance();
  }

}
