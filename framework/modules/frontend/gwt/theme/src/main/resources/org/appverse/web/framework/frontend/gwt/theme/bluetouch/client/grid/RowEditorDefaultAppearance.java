/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.theme.base.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing.RowEditorAppearance;

public class RowEditorDefaultAppearance implements RowEditorAppearance {

  public interface RowEditorResources extends ClientBundle {
    @Source("RowEditor.css")
    RowEditorStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource editorButtonLeft();

    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource editorButtonRight();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource editorButtonBackground();
  }

  public interface RowEditorStyle extends CssResource {
    String editor();

    String outer();

    String inner();

    String body();

    String buttons();

    String buttonsLeft();

    String buttonsRight();

    String buttonsContent();
    
    String label();
  }

  public interface Template extends XTemplates {
    @XTemplate(source = "RowEditor.html")
    SafeHtml render(RowEditorStyle style);
  }

  private final RowEditorResources resources;
  private final RowEditorStyle style;
  protected Template template = GWT.create(Template.class);

  public RowEditorDefaultAppearance() {
    this(GWT.<RowEditorResources> create(RowEditorResources.class));
  }

  public RowEditorDefaultAppearance(RowEditorResources resources) {
    this.resources = resources;
    this.style = resources.css();
    StyleInjectorHelper.ensureInjected(resources.css(), true);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(resources.css()));
  }

  @Override
  public XElement getButtonWrap(XElement parent) {
    return parent.selectNode("." + style.buttonsContent());
  }

  @Override
  public XElement getContentWrap(XElement parent) {
    return parent.selectNode("." + style.body());
  }
  
  @Override
  public String labelClass() {
    return style.label();
  }

}
