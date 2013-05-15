/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem.LabelToolItemAppearance;

public class AppverseWebLabelToolItemAppearance implements LabelToolItemAppearance {

  public interface LabelToolItemResources extends ClientBundle {

    @Source("LabelToolItem.css")
    LabelToolItemStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource background();
  }

  public interface LabelToolItemStyle extends CssResource {

    String item();
  }

  public interface Template extends XTemplates {
    @XTemplate("<div class=\"{style.item}\"></div>")
    SafeHtml render(LabelToolItemStyle style);
  }

  protected LabelToolItemStyle style;
  protected Template template;

  public AppverseWebLabelToolItemAppearance() {
    this(GWT.<LabelToolItemResources> create(LabelToolItemResources.class));
  }

  public AppverseWebLabelToolItemAppearance(LabelToolItemResources resources) {
    this.style = resources.css();
    
    StyleInjectorHelper.ensureInjected(this.style, true);
    
    this.template = GWT.create(Template.class);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

}
