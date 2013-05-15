/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;

public class AppverseWebHeaderAppearance extends HeaderDefaultAppearance {

  public interface AppverseWebHeaderStyle extends HeaderStyle {
    String header();

    String headerIcon();

    String headerHasIcon();

    String headerText();

    String headerBar();
  }

  public interface AppverseWebHeaderResources extends HeaderResources {

    @Source({"org/appverse/web/framework/frontend/gwt/theme/bluetouch/client/widget/Header.css", "AppverseWebHeader.css"})
    AppverseWebHeaderStyle style();
    
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }
  

  public AppverseWebHeaderAppearance() {
    this(GWT.<AppverseWebHeaderResources> create(AppverseWebHeaderResources.class),
        GWT.<Template> create(Template.class));
  }

  public AppverseWebHeaderAppearance(HeaderResources resources, Template template) {
    super(resources, template);
  }

}
