/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.panel;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.panel.AccordionLayoutBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

public class AppverseWebAccordionLayoutAppearance extends AccordionLayoutBaseAppearance {
  
  public interface AppverseWebAccordionLayoutResources extends ContentPanelResources {

    @Source({"com/sencha/gxt/theme/base/client/panel/ContentPanel.css", "AppverseWebContentPanel.css"})
    @Override
    AppverseWebAccordionLayoutStyle style();

  }

  public interface AppverseWebAccordionLayoutStyle extends ContentPanelStyle {

  }
  
  public AppverseWebAccordionLayoutAppearance() {
    super(GWT.<AppverseWebAccordionLayoutResources> create(AppverseWebAccordionLayoutResources.class));
  }

  public AppverseWebAccordionLayoutAppearance(AppverseWebAccordionLayoutResources resources) {
    super(resources);
  }
  
  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new AppverseWebAccordionHeaderAppearance();
  }
}
