package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.container;


import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.container.BoxLayoutDefaultAppearance;

/**
 * NEW IN APPVERSEWEBTHEME: This class does not exist in original Sencha GXT
 * "Blue" theme. We have added it to overwrite BoxLayout CSS
 * 
 */
public class AppverseWebBoxLayoutDefaultAppearance extends BoxLayoutDefaultAppearance {

  public interface AppverseWebBoxLayoutBaseResources extends BoxLayoutBaseResources {

	@Override
	@Source({ "com/sencha/gxt/theme/base/client/container/BoxLayout.css",
				"AppverseWebBoxLayout.css" })
    AppverseWebBoxLayoutStyle style();

  }

  public interface AppverseWebBoxLayoutStyle extends BoxLayoutStyle {

  }
    

  public AppverseWebBoxLayoutDefaultAppearance() {
    super(GWT.<AppverseWebBoxLayoutBaseResources> create(AppverseWebBoxLayoutBaseResources.class),
        GWT.<BoxLayoutTemplate> create(BoxLayoutTemplate.class));
  }
  
}