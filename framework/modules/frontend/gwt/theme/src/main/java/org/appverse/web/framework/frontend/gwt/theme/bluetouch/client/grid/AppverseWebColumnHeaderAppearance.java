/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.grid;

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
import com.sencha.gxt.widget.core.client.grid.ColumnHeader.ColumnHeaderAppearance;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader.ColumnHeaderStyles;

public class AppverseWebColumnHeaderAppearance implements ColumnHeaderAppearance {

  public interface ColumnHeaderResources extends ClientBundle {

    // preventInlining only need for ie6 ie7 because of bottom alignment
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, preventInlining = true)
    ImageResource columnHeader();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource columnHeaderOver();

    ImageResource columnsIcon();

    ImageResource sortAsc();

    ImageResource sortDesc();
    
    ImageResource sortAscendingIcon();
    
    ImageResource sortDescendingIcon();
    
    ImageResource columnMoveTop();
    
    ImageResource columnMoveBottom();

    @Source("ColumnHeader.css")
    ColumnHeaderStyle style();

  }

  public interface ColumnHeaderStyle extends CssResource, ColumnHeaderStyles {

  }

  public interface ColumnHeaderTemplate extends XTemplates {
    @XTemplate(source = "ColumnHeader.html")
    SafeHtml render(ColumnHeaderStyle style);
  }

  private final ColumnHeaderResources resources;
  private final ColumnHeaderStyle style;
  private ColumnHeaderTemplate templates = GWT.create(ColumnHeaderTemplate.class);

  public AppverseWebColumnHeaderAppearance() {
    this(GWT.<ColumnHeaderResources> create(ColumnHeaderResources.class));
  }

  public AppverseWebColumnHeaderAppearance(ColumnHeaderResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public ImageResource columnsIcon() {
    return resources.columnsIcon();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(templates.render(style));
  }

  @Override
  public ImageResource sortAscendingIcon() {
    return resources.sortAscendingIcon();
  }

  @Override
  public ImageResource sortDescendingIcon() {
    return resources.sortDescendingIcon();
  }

  @Override
  public ColumnHeaderStyles styles() {
    return style;
  }

  @Override
  public String columnsWrapSelector() {
    return "." + style.headerInner();
  }

}
