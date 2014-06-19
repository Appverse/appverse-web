/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.window;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame.NestedDivFrameStyle;
import com.sencha.gxt.theme.base.client.panel.FramedPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance.HeaderResources;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance.HeaderStyle;
import com.sencha.gxt.widget.core.client.Window.WindowAppearance;
import org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.panel.AppverseWebFramedPanelAppearance.FramedPanelStyle;

public class AppverseWebWindowAppearance extends FramedPanelBaseAppearance implements WindowAppearance {

  public interface AppverseWebWindowDivFrameStyle extends NestedDivFrameStyle {

  }

  public interface AppverseWebWindowDivFrameResources extends FramedPanelDivFrameResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/frame/NestedDivFrame.css", "AppverseWebWindowDivFrame.css"})
    @Override
    AppverseWebWindowDivFrameStyle style();

    @Source("com/sencha/gxt/theme/base/client/shared/clear.gif")
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

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomRightBorder();

  }

  public interface AppverseWebWindowStyle extends FramedPanelStyle {
    String ghost();
  }

  public interface AppverseWebHeaderStyle extends HeaderStyle {

  }

  public interface AppverseWebHeaderResources extends HeaderResources {
    @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "AppverseWebWindowHeader.css"})
    AppverseWebHeaderStyle style();
  }

  public interface AppverseWebWindowResources extends ContentPanelResources, ClientBundle {

    @Source({
        "com/sencha/gxt/theme/base/client/panel/ContentPanel.css",
        "com/sencha/gxt/theme/base/client/window/Window.css", "AppverseWebWindow.css"})
    @Override
    AppverseWebWindowStyle style();

  }

  private AppverseWebWindowStyle style;

  public AppverseWebWindowAppearance() {
    this((AppverseWebWindowResources) GWT.create(AppverseWebWindowResources.class));
  }

  public AppverseWebWindowAppearance(AppverseWebWindowResources resources) {
    super(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), new NestedDivFrame(
        GWT.<AppverseWebWindowDivFrameResources> create(AppverseWebWindowDivFrameResources.class)));

    this.style = resources.style();
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new HeaderDefaultAppearance(GWT.<AppverseWebHeaderResources> create(AppverseWebHeaderResources.class));
  }

  @Override
  public String ghostClass() {
    return style.ghost();
  }
}
