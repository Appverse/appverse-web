/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel.PlainTabPanelAppearance;

/**
 * A blue-colored appearance for {@link PlainTabPanel}. This appearance differs
 * from {@link DefaultTabPanelAppearance} in that it has a simplified tab strip.
 */
public class DefaultPlainTabPanelAppearance extends DefaultTabPanelAppearance implements PlainTabPanelAppearance {

  public interface BluePlainTabPanelResources extends BlueTabPanelResources {

    @Source({
        "com/sencha/gxt/theme/base/client/tabs/TabPanel.css", "BlueTabPanel.css",
        "com/sencha/gxt/theme/base/client/tabs/PlainTabPanel.css", "BluePlainTabPanel.css"})
    BluePlainTabPanelStyle style();

  }

  public interface BluePlainTabPanelStyle extends BlueTabPanelStyle {

    String tabStripSpacer();

  }

  public interface PlainTabPanelTemplates extends Template {

    @XTemplate(source = "com/sencha/gxt/theme/base/client/tabs/TabPanel.html")
    SafeHtml render(TabPanelStyle style);

    @XTemplate(source = "com/sencha/gxt/theme/base/client/tabs/PlainTabPanel.html")
    SafeHtml renderPlain(BluePlainTabPanelStyle style);

  }

  private final PlainTabPanelTemplates template;
  private final BluePlainTabPanelStyle style;

  public DefaultPlainTabPanelAppearance() {
    this(GWT.<BluePlainTabPanelResources> create(BluePlainTabPanelResources.class),
        GWT.<PlainTabPanelTemplates> create(PlainTabPanelTemplates.class),
        GWT.<ItemTemplate> create(ItemTemplate.class));
  }

  public DefaultPlainTabPanelAppearance(BluePlainTabPanelResources resources, PlainTabPanelTemplates template,
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
