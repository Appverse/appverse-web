/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.util.IconHelper;
import com.sencha.gxt.widget.core.client.Header.HeaderAppearance;

public class AppvHeaderDefaultAppearance implements HeaderAppearance {

  public interface HeaderStyle extends CssResource {
    String header();

    String headerBar();

    String headerHasIcon();

    String headerIcon();

    String headerText();
  }

  public interface HeaderResources extends ClientBundle {

    @Source("Header.css")
    HeaderStyle style();
  }

  public interface Template extends XTemplates {
    @XTemplate(source = "Header.html")
    SafeHtml render(HeaderStyle style);
  }

  private final HeaderResources resources;
  private Template template;
  private final HeaderStyle style;

  public AppvHeaderDefaultAppearance() {
    this(GWT.<HeaderResources> create(HeaderResources.class), GWT.<Template> create(Template.class));
  }

  public AppvHeaderDefaultAppearance(HeaderResources resources) {
    this(resources, GWT.<Template> create(Template.class));
  }

  public AppvHeaderDefaultAppearance(HeaderResources resources, Template template) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);

    this.template = template;
  }

  @Override
  public XElement getBarElem(XElement parent) {
    return parent.getChild(1).cast();
  }

  @Override
  public XElement getTextElem(XElement parent) {
    return parent.getChild(2).cast();
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(template.render(style));
  }

  @Override
  public void setIcon(XElement parent, ImageResource icon) {
    XElement iconWrap = parent.getFirstChildElement().cast();
    iconWrap.removeChildren();
    if (icon != null) {
      iconWrap.appendChild(IconHelper.getElement(icon));
    }
    parent.setClassName(style.headerHasIcon(), icon != null);
  }

}
