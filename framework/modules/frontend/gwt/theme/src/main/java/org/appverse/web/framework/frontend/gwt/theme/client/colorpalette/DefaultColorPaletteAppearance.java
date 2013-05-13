/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.colorpalette;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.sencha.gxt.theme.base.client.colorpalette.ColorPaletteBaseAppearance;

public class DefaultColorPaletteAppearance extends ColorPaletteBaseAppearance {

  public interface BlueColorPaletteResources extends ColorPaletteBaseAppearance.ColorPaletteResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/colorpalette/ColorPalette.css", "BlueColorPalette.css"})
    BlueColorPaletteStyle style();

  }

  public interface BlueColorPaletteStyle extends ColorPaletteStyle {
  }

  public DefaultColorPaletteAppearance() {
    this(GWT.<BlueColorPaletteResources> create(BlueColorPaletteResources.class),
        GWT.<BaseColorPaletteTemplate> create(BaseColorPaletteTemplate.class));
  }

  public DefaultColorPaletteAppearance(BlueColorPaletteResources resources, BaseColorPaletteTemplate template) {
    super(resources, template);
  }

}
