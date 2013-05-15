/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.status;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.status.StatusBaseAppearance;

public class AppverseWebStatusAppearance extends StatusBaseAppearance {

  public interface AppverseWebStatusResources extends StatusBaseAppearance.StatusResources, ClientBundle {

    @Override
    @Source({"com/sencha/gxt/theme/base/client/status/Status.css", "AppverseWebStatus.css"})
    StatusStyle style();

    @Override
    @Source("com/sencha/gxt/theme/base/client/grid/loading.gif")
    ImageResource loading();

  }

  public AppverseWebStatusAppearance() {
    super(GWT.<StatusResources> create(AppverseWebStatusResources.class), GWT.<Template> create(Template.class));
  }
  
  public AppverseWebStatusAppearance(AppverseWebStatusResources resources, Template template) {
    super(resources, template);
  }

}
