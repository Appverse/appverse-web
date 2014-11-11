/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.form.FieldLabel.FieldLabelAppearance;
import com.sencha.gxt.widget.core.client.form.FieldLabel.FieldLabelOptions;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;

public class AppverseWebFieldLabelAppearance implements FieldLabelAppearance {

  public interface FieldLabelResources extends ClientBundle {

    @Source("FieldLabel.css")
    Style css();

  }

  public interface Style extends CssResource {

    String clearLeft();

    String fieldElement();

    String fieldItem();

    String fieldItemLabelTop();

    String fieldLabel();

    String hideLabel();

  }

  public interface FieldLabelTemplate extends XTemplates {

    @XTemplate(source = "FieldLabel.html")
    SafeHtml render(String id, Style style, SafeStyles fieldLabelStyles, SafeStyles fieldElementStyles);

  }

  private FieldLabelTemplate template;
  private Style style;

  public AppverseWebFieldLabelAppearance() {
    this(GWT.<FieldLabelResources> create(FieldLabelResources.class), GWT.<FieldLabelTemplate> create(FieldLabelTemplate.class));
  }

  public AppverseWebFieldLabelAppearance(FieldLabelResources resources, FieldLabelTemplate template) {
    this.template = template;

    this.style = resources.css();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public void clearLabelFor(XElement parent) {
    getLabelElement(parent).removeAttribute("for");
  }

  @Override
  public XElement getChildElementWrapper(XElement parent) {
    // second child of parent
    XElement childElementWrapper = XElement.as(parent.getChild(1));
    assert childElementWrapper.is("div." + style.fieldElement());
    return childElementWrapper;
  }

  public XElement getLabelElement(XElement parent) {
    XElement labelElement = XElement.as(parent.getFirstChildElement());
    assert labelElement.is("label." + style.fieldLabel());
    return labelElement;
  }

  @Override
  public String getLabelHtml(XElement parent) {
    return getLabelElement(parent).getInnerHTML();
  }

  @Override
  public String getLabelText(XElement parent) {
    return getLabelElement(parent).getInnerText();
  }

  public void onUpdateOptions(XElement parent, FieldLabelOptions options) {
    LabelAlign labelAlign = options.getLabelAlign();
    XElement fieldElement = getChildElementWrapper(parent);
    XElement labelElement = getLabelElement(parent);

    // Adjust for label content, label separator
    if (options.isHtmlContent()) {
      labelElement.setInnerHTML(options.getContent() + options.getLabelSeparator());
    } else {
      labelElement.setInnerText(options.getContent() + options.getLabelSeparator());
    }

    // Adjust for label alignment
    if (labelAlign == LabelAlign.TOP) {
      parent.addClassName(style.fieldItemLabelTop());
    } else {
      parent.removeClassName(style.fieldItemLabelTop());
    }

    // Adjust for label width
    if (labelAlign == LabelAlign.TOP) {
      labelElement.getStyle().setProperty("width", "auto");
      fieldElement.getStyle().setPaddingLeft(0, Unit.PX);
    } else {
      int pad = options.getLabelPad();
      if (pad == 0) pad = 5;
      labelElement.getStyle().setWidth(options.getLabelWidth(), Unit.PX);
      fieldElement.getStyle().setPaddingLeft(options.getLabelWidth() + pad, Unit.PX);
    }
  }

  @Override
  public void render(SafeHtmlBuilder sb, String id, FieldLabelOptions options) {
    int labelWidth = options.getLabelWidth();
    LabelAlign align = options.getLabelAlign();

    int pad = options.getLabelPad();
    if (pad == 0) pad = 5;

    String fieldLabelWidth = align == LabelAlign.TOP ? "auto" : (labelWidth + "px");
    SafeStyles fieldLabelStyles = SafeStylesUtils.fromTrustedString("width:" + fieldLabelWidth + ";");

    String fieldElementPadding = align == LabelAlign.TOP ? "0" : (labelWidth + pad + "px");
    SafeStyles fieldElementStyles = SafeStylesUtils.fromTrustedString("padding-left:" + fieldElementPadding + ";");

    sb.append(template.render(id, style, fieldLabelStyles, fieldElementStyles));
  }

  @Override
  public void setLabelFor(XElement parent, String id) {
    getLabelElement(parent).setAttribute("for", id + "-input");
  }

}
