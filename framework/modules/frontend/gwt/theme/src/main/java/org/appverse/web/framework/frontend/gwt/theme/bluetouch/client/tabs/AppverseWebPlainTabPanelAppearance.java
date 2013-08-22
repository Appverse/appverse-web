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

/**
 * An appverseweb-colored appearance for {@link PlainTabPanel}. This appearance differs
 * from {@link AppverseWebTabPanelAppearance} in that it has a simplified tab strip.
 */
public class AppverseWebPlainTabPanelAppearance extends AppverseWebTabPanelAppearance implements PlainTabPanelAppearance {

  public interface AppverseWebPlainTabPanelResources extends AppverseWebTabPanelResources {

    @Source({
        "com/sencha/gxt/theme/base/client/tabs/TabPanel.css", "AppverseWebTabPanel.css",
        "com/sencha/gxt/theme/base/client/tabs/PlainTabPanel.css", "AppverseWebPlainTabPanel.css"})
    AppverseWebPlainTabPanelStyle style();

  }

  public interface AppverseWebPlainTabPanelStyle extends AppverseWebTabPanelStyle {

    String tabStripSpacer();
    String tabStripText();
  }

  public interface PlainTabPanelTemplates extends Template {

    @XTemplate(source = "com/sencha/gxt/theme/base/client/tabs/TabPanel.html")
    SafeHtml render(TabPanelStyle style);

    @XTemplate(source = "com/sencha/gxt/theme/base/client/tabs/PlainTabPanel.html")
    SafeHtml renderPlain(AppverseWebPlainTabPanelStyle style);

  }

  private final PlainTabPanelTemplates template;
  private final AppverseWebPlainTabPanelStyle style;

  public AppverseWebPlainTabPanelAppearance() {
    this(GWT.<AppverseWebPlainTabPanelResources> create(AppverseWebPlainTabPanelResources.class),
        GWT.<PlainTabPanelTemplates> create(PlainTabPanelTemplates.class),
        GWT.<ItemTemplate> create(ItemTemplate.class));
  }

  public AppverseWebPlainTabPanelAppearance(AppverseWebPlainTabPanelResources resources, PlainTabPanelTemplates template,
      ItemTemplate itemTemplate) {
    super(resources, template, itemTemplate);
    this.style = resources.style();
    this.template = template;
  }

  @Override
  public void render(SafeHtmlBuilder builder) {
    builder.append(template.renderPlain(style));
  }

}
