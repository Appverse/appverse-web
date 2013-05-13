/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.panel;

import org.appverse.web.framework.frontend.gwt.theme.client.widget.AppvHeaderDefaultAppearance;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

public class DefaultContentPanelAppearance extends ContentPanelBaseAppearance {

  public interface BlueContentPanelResources extends ContentPanelResources {

    @Source({"com/sencha/gxt/theme/base/client/panel/ContentPanel.css", "BlueContentPanel.css"})
    @Override
    BlueContentPanelStyle style();

  }

  public interface BlueContentPanelStyle extends ContentPanelStyle {

  }

  public DefaultContentPanelAppearance() {
    super(GWT.<BlueContentPanelResources> create(BlueContentPanelResources.class),
        GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  public DefaultContentPanelAppearance(BlueContentPanelResources resources) {
    super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
	  return new HeaderDefaultAppearance();
//	  return new AppvHeaderDefaultAppearance();
//    return new DefaultHeaderAppearance();
  }

}
