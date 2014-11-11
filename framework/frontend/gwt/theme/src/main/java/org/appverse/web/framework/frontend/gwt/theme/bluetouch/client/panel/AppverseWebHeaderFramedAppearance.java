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

public class AppverseWebHeaderFramedAppearance extends AppverseWebHeaderAppearance {

  public interface AppverseWebHeaderFramedStyle extends HeaderStyle {

  }

  public interface AppverseWebFramedHeaderResources extends HeaderResources {

    @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "AppverseWebHeader.css", "AppverseWebFramedHeader.css"})
    AppverseWebHeaderFramedStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }

  public AppverseWebHeaderFramedAppearance() {
    this(GWT.<AppverseWebFramedHeaderResources> create(AppverseWebFramedHeaderResources.class), GWT.<Template> create(Template.class));
  }

  public AppverseWebHeaderFramedAppearance(AppverseWebFramedHeaderResources resources, Template template) {
    super(resources, template);
  }
}
