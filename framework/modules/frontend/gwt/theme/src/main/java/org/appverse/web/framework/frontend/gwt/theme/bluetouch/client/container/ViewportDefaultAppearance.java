package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.container.Viewport.ViewportAppearance;

public class ViewportDefaultAppearance implements ViewportAppearance {

  public interface ViewportResources extends ClientBundle {
	  
	@Source("background.png")
	@ImageOptions(repeatStyle=RepeatStyle.Both)
	public ImageResource background();	  

	@Source({ "com/sencha/gxt/theme/base/client/container/Viewport.css",
	"AppverseWebViewport.css" })
    ViewportStyle style();

  }

  public interface ViewportStyle extends CssResource {

    String viewport();

  }

  public interface ViewportTemplate extends XTemplates {

    @XTemplate("<div class=\"{style.viewport}\"></div>")
    SafeHtml template(ViewportStyle style);

  }

  private final ViewportResources resources;
  private final ViewportTemplate templates;
  
  public ViewportDefaultAppearance() {
    this(GWT.<ViewportResources>create(ViewportResources.class), GWT.<ViewportTemplate>create(ViewportTemplate.class));
  }

  public ViewportDefaultAppearance(ViewportResources resources, ViewportTemplate templates) {
    this.resources = resources;
    this.templates = templates;
    
    StyleInjectorHelper.ensureInjected(resources.style(), true);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(templates.template(resources.style()));
  }

}
