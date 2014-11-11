/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.cell.core.client.form.FieldCell.FieldAppearanceOptions;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.theme.base.client.field.TextFieldDefaultAppearance.TextFieldStyle;
import com.sencha.gxt.theme.base.client.field.ValueBaseFieldDefaultAppearance;

public class AppverseWebTriggerFieldAppearance extends ValueBaseFieldDefaultAppearance implements TriggerFieldAppearance {

  public interface TriggerFieldResources extends ValueBaseFieldResources, ClientBundle {

    @Source({"ValueBaseField.css", "TextField.css", "TriggerField.css"})
    TriggerFieldStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, preventInlining = true)
    ImageResource invalidLine();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource textBackground();

    ImageResource triggerArrow();

    ImageResource triggerArrowClick();

    ImageResource triggerArrowFocus();

    ImageResource triggerArrowFocusClick();

    ImageResource triggerArrowFocusOver();

    ImageResource triggerArrowOver();
  }

  public interface TriggerFieldStyle extends TextFieldStyle {

    String click();

    String noedit();

    String over();

    String trigger();

  }

  private final TriggerFieldResources resources;
  private final TriggerFieldStyle style;

  public AppverseWebTriggerFieldAppearance() {
    this(GWT.<TriggerFieldResources> create(TriggerFieldResources.class));
  }

  public AppverseWebTriggerFieldAppearance(TriggerFieldResources resources) {
    super(resources);
    this.resources = resources;
    this.style = resources.css();
  }

  @Override
  public XElement getInputElement(Element parent) {
    return parent.<XElement> cast().selectNode("input");
  }

  @Override
  public void onFocus(Element parent, boolean focus) {
    parent.<XElement> cast().setClassName(resources.css().focus(), focus);
    getInputElement(parent).setClassName(resources.css().focus(), focus);
  }

  @Override
  public void onTriggerClick(XElement parent, boolean click) {
    getTrigger(parent).setClassName(resources.css().click(), click);
  }

  @Override
  public void onTriggerOver(XElement parent, boolean over) {
    parent.setClassName(resources.css().over(), over);
  }

  @Override
  public void render(SafeHtmlBuilder sb, String value, FieldAppearanceOptions options) {
    int width = options.getWidth();
    boolean hideTrigger = options.isHideTrigger();

    if (width == -1) {
      width = 150;
    }

    SafeStyles inputStyles = null;
    String wrapStyles = "";

    if (width != -1) {
      wrapStyles += "width:" + width + "px;";

      // 6px margin, 2px border
      width -= 8;

      if (!hideTrigger) {
        width -= resources.triggerArrow().getWidth();
      }
      inputStyles = SafeStylesUtils.fromTrustedString("width:" + width + "px;");
    }

    sb.appendHtmlConstant("<div style='" + wrapStyles + "' class='" + style.wrap() + "'>");

    if (!hideTrigger) {
      sb.appendHtmlConstant("<table cellpadding=0 cellspacing=0><tr><td>");
      renderInput(sb, value, inputStyles, options);
      sb.appendHtmlConstant("</td>");
      sb.appendHtmlConstant("<td><div class='" + style.trigger() + "' /></td>");
      sb.appendHtmlConstant("</table>");
    } else {
      renderInput(sb, value, inputStyles, options);
    }

    sb.appendHtmlConstant("</div>");
  }

  /**
   * Helper method to render the input in the trigger field. 
   */
  private void renderInput(SafeHtmlBuilder shb, String value, SafeStyles inputStyles,
      FieldAppearanceOptions options) {
    // Deliberately using a StringBuilder, not SafeHtmlBuilder, as each append isn't adding
    // complete elements, but building up this single element, one attribute at a time.
    StringBuilder sb = new StringBuilder();
    sb.append("<input ");
    
    if (options.isDisabled()) {
      sb.append("disabled=true ");
    }

    if (options.getName() != null) {
      // if set, escape the name property so it is a valid attribute
      sb.append("name='").append(SafeHtmlUtils.htmlEscape(options.getName())).append("' ");
    }

    if (options.isReadonly() || !options.isEditable()) {
      sb.append("readonly ");
    }
    
    if (inputStyles != null) {
      sb.append("style='").append(inputStyles.asString()).append("' ");
    }
    

    sb.append("class='").append(style.field()).append(" ").append(style.text());

    if (value.equals("") && options.getEmptyText() != null) {
      sb.append(" ").append(style.empty());
      value = options.getEmptyText();
    }

    if (!options.isEditable()) {
      sb.append(" ").append(style.noedit());
    }
    sb.append("' ");

    //escaping the value string so it is a valid attribute
    sb.append("type='text' value='").append(SafeHtmlUtils.htmlEscape(value)).append("'/>");

    // finally, converting stringbuilder into a SafeHtml instance and appending it
    // to the buidler we were given
    shb.append(SafeHtmlUtils.fromTrustedString(sb.toString()));
  }

  @Override
  public void setEditable(XElement parent, boolean editable) {
    getInputElement(parent).setClassName(style.noedit(), !editable);
  }

  @Override
  public void setTriggerVisible(XElement parent, boolean visible) {
    getTrigger(parent).setVisible(visible);
  }

  @Override
  public boolean triggerIsOrHasChild(XElement parent, Element target) {
    return parent.isOrHasChild(target) && target.<XElement> cast().is("." + resources.css().trigger());
  }

  protected XElement getTrigger(Element parent) {
    return parent.<XElement> cast().selectNode("." + resources.css().trigger());
  }

  @Override
  public void onResize(XElement parent, int width, int height, boolean hideTrigger) {
    if (width != -1) {
      Element div = parent.getFirstChildElement();
      div.getStyle().setPropertyPx("width", width);

      // 6px margin, 2px border
      width -= 8;

      if (!hideTrigger) {
        width -= resources.triggerArrow().getWidth();
      }
      parent.selectNode("input").getStyle().setPropertyPx("width", width);
    }
  }

}