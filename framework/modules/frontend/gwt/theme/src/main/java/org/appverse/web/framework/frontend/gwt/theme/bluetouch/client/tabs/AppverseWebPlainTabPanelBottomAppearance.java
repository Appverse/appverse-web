/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel.PlainTabPanelAppearance;
import org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.tabs.AppverseWebPlainTabPanelAppearance.AppverseWebPlainTabPanelResources;
import org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.tabs.AppverseWebPlainTabPanelAppearance.AppverseWebPlainTabPanelStyle;

/**
 * An appverseweb-coloured appearance for {@link PlainTabPanel} with tabs below the
 * content area. This appearance differs from
 * {@link AppverseWebTabPanelBottomAppearance} in that it has a simplified tab strip.
 */
public class AppverseWebPlainTabPanelBottomAppearance extends AppverseWebTabPanelBottomAppearance implements PlainTabPanelAppearance {

  public interface PlainTabPanelBottomTemplates extends BottomTemplate {

    @XTemplate(source = "TabPanelBottom.html")
    SafeHtml render(TabPanelStyle style);

    @XTemplate(source = "PlainTabPanelBottom.html")
    SafeHtml renderPlain(AppverseWebPlainTabPanelStyle style);

  }

  protected PlainTabPanelBottomTemplates template;
  protected AppverseWebPlainTabPanelResources resources;

  public AppverseWebPlainTabPanelBottomAppearance() {
    this(GWT.<AppverseWebPlainTabPanelResources> create(AppverseWebPlainTabPanelResources.class),
        GWT.<PlainTabPanelBottomTemplates> create(PlainTabPanelBottomTemplates.class),
        GWT.<ItemTemplate> create(ItemTemplate.class));
  }

  public AppverseWebPlainTabPanelBottomAppearance(AppverseWebPlainTabPanelResources resources, PlainTabPanelBottomTemplates template,
      ItemTemplate itemTemplate) {
    super((AppverseWebTabPanelResources)resources, template, itemTemplate);
    this.resources = resources;
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.renderPlain(resources.style()));
  }

}
