/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.colorpalette;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.sencha.gxt.theme.base.client.colorpalette.ColorPaletteBaseAppearance;

public class AppverseWebColorPaletteAppearance extends ColorPaletteBaseAppearance {

  public interface AppverseWebColorPaletteResources extends ColorPaletteBaseAppearance.ColorPaletteResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/colorpalette/ColorPalette.css", "AppverseWebColorPalette.css"})
    AppverseWebColorPaletteStyle style();

  }

  public interface AppverseWebColorPaletteStyle extends ColorPaletteStyle {
  }

  public AppverseWebColorPaletteAppearance() {
    this(GWT.<AppverseWebColorPaletteResources> create(AppverseWebColorPaletteResources.class),
        GWT.<BaseColorPaletteTemplate> create(BaseColorPaletteTemplate.class));
  }

  public AppverseWebColorPaletteAppearance(AppverseWebColorPaletteResources resources, BaseColorPaletteTemplate template) {
    super(resources, template);
  }

}
