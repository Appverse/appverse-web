/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.window;

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
import com.sencha.gxt.theme.blue.client.panel.BlueFramedPanelAppearance.FramedPanelStyle;
import com.sencha.gxt.widget.core.client.Window.WindowAppearance;

public class DefaultWindowAppearance extends FramedPanelBaseAppearance implements WindowAppearance {

  public interface BlueWindowDivFrameStyle extends NestedDivFrameStyle {

  }

  public interface BlueWindowDivFrameResources extends FramedPanelDivFrameResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/frame/NestedDivFrame.css", "BlueWindowDivFrame.css"})
    @Override
    BlueWindowDivFrameStyle style();

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

  public interface BlueWindowStyle extends FramedPanelStyle {
    String ghost();
  }

  public interface BlueHeaderStyle extends HeaderStyle {

  }

  public interface BlueHeaderResources extends HeaderResources {
    @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "BlueWindowHeader.css"})
    BlueHeaderStyle style();
  }

  public interface BlueWindowResources extends ContentPanelResources, ClientBundle {

    @Source({
        "com/sencha/gxt/theme/base/client/panel/ContentPanel.css",
        "com/sencha/gxt/theme/base/client/window/Window.css", "BlueWindow.css"})
    @Override
    BlueWindowStyle style();

  }

  private BlueWindowStyle style;

  public DefaultWindowAppearance() {
    this((BlueWindowResources) GWT.create(BlueWindowResources.class));
  }

  public DefaultWindowAppearance(BlueWindowResources resources) {
    super(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), new NestedDivFrame(
        GWT.<BlueWindowDivFrameResources> create(BlueWindowDivFrameResources.class)));

    this.style = resources.style();
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new HeaderDefaultAppearance(GWT.<BlueHeaderResources> create(BlueHeaderResources.class));
  }

  @Override
  public String ghostClass() {
    return style.ghost();
  }
}
