/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.panel;

import org.appverse.web.framework.frontend.gwt.theme.client.widget.AppvHeaderDefaultAppearance;
import org.appverse.web.framework.frontend.gwt.theme.client.widget.AppvHeaderDefaultAppearance.HeaderStyle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public class AppvHeaderAppearance extends AppvHeaderDefaultAppearance {

  public interface BlueHeaderStyle extends HeaderStyle {
    String header();

    String headerIcon();

    String headerHasIcon();

    String headerText();

    String headerBar();
  }

  public interface BlueHeaderResources extends HeaderResources {

    @Source({"org/appverse/web/framework/frontend/gwt/theme/client/widget/Header.css", "BlueHeader.css"})
    BlueHeaderStyle style();
    
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }
  

  public AppvHeaderAppearance() {
    this(GWT.<BlueHeaderResources> create(BlueHeaderResources.class),
        GWT.<Template> create(Template.class));
  }

  public AppvHeaderAppearance(HeaderResources resources, Template template) {
    super(resources, template);
  }

}
