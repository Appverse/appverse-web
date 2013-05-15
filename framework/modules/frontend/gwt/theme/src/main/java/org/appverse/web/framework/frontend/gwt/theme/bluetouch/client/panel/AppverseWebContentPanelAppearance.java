/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.panel;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.panel.ContentPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

public class AppverseWebContentPanelAppearance extends ContentPanelBaseAppearance {

  public interface AppverseWebContentPanelResources extends ContentPanelResources {

    @Source({"com/sencha/gxt/theme/base/client/panel/ContentPanel.css", "AppverseWebContentPanel.css"})
    @Override
    AppverseWebContentPanelStyle style();

  }

  public interface AppverseWebContentPanelStyle extends ContentPanelStyle {

  }

  public AppverseWebContentPanelAppearance() {
    super(GWT.<AppverseWebContentPanelResources> create(AppverseWebContentPanelResources.class),
        GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  public AppverseWebContentPanelAppearance(AppverseWebContentPanelResources resources) {
    super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
	  return new AppverseWebHeaderAppearance();
  }

}
