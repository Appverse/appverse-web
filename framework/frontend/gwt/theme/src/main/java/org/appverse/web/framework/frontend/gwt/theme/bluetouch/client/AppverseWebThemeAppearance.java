/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.resources.ThemeStyles.Styles;
import com.sencha.gxt.core.client.resources.ThemeStyles.ThemeAppearance;

public class AppverseWebThemeAppearance implements ThemeAppearance {

  static interface Bundle extends ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/BaseTheme.css", "BlueTouchTheme.css"})
    Styles css();

    ImageResource more();
  }

  private Bundle bundle;
  private Styles style;

  @Override
  public Styles style() {
    return style;
  }

  public AppverseWebThemeAppearance() {
    this.bundle = GWT.create(Bundle.class);
    this.style = bundle.css();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public ImageResource moreIcon() {
    return bundle.more();
  }

}
