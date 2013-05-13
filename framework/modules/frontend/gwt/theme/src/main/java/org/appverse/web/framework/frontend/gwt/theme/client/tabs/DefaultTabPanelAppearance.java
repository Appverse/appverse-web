/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.tabs;

import static com.google.gwt.resources.client.ImageResource.RepeatStyle.Both;
import static com.google.gwt.resources.client.ImageResource.RepeatStyle.Horizontal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.sencha.gxt.theme.base.client.tabs.TabPanelBaseAppearance;

public class DefaultTabPanelAppearance extends TabPanelBaseAppearance {

  public interface BlueTabPanelResources extends TabPanelResources, ClientBundle {

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomInactiveLeftBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomInactiveRightBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomLeftBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomRightBackground();

    ImageResource scrollerLeft();

    ImageResource scrollerLeftOver();

    ImageResource scrollerRight();

    ImageResource scrollerRightOver();

    @Source({"com/sencha/gxt/theme/base/client/tabs/TabPanel.css", "BlueTabPanel.css"})
    BlueTabPanelStyle style();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenter();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenterActive();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenterOver();

    ImageResource tabClose();

    ImageResource tabLeft();

    ImageResource tabLeftActive();

    ImageResource tabLeftOver();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRight();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRightActive();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRightOver();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabStripBackground();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabStripBottomBackground();

  }

  public interface BlueTabPanelStyle extends TabPanelStyle {
  }

  public DefaultTabPanelAppearance() {
    this(GWT.<BlueTabPanelResources> create(BlueTabPanelResources.class), GWT.<Template> create(Template.class),
        GWT.<ItemTemplate> create(ItemTemplate.class));
  }

  public DefaultTabPanelAppearance(BlueTabPanelResources resources, Template template, ItemTemplate itemTemplate) {
    super(resources, template, itemTemplate);
  }

}
