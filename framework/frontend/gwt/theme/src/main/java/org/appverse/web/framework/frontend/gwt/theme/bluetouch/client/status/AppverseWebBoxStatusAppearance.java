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
import com.sencha.gxt.theme.base.client.status.BoxStatusBaseAppearance;
import com.sencha.gxt.widget.core.client.Status.BoxStatusAppearance;

public class AppverseWebBoxStatusAppearance extends BoxStatusBaseAppearance implements BoxStatusAppearance {

  public interface AppverseWebBoxStatusStyle extends BoxStatusStyle {

    String status();

    String statusIcon();

    String statusText();

    String statusBox();

  }

  public interface AppverseWebBoxStatusResources extends BoxStatusResources, ClientBundle {

    @Override
    @Source({"com/sencha/gxt/theme/base/client/status/Status.css", "AppverseWebBoxStatus.css"})
    AppverseWebBoxStatusStyle style();

    @Override
    @Source("com/sencha/gxt/theme/base/client/grid/loading.gif")
    ImageResource loading();

  }

  public AppverseWebBoxStatusAppearance() {
    this(GWT.<AppverseWebBoxStatusResources> create(AppverseWebBoxStatusResources.class), GWT.<BoxTemplate> create(BoxTemplate.class));
  }

  public AppverseWebBoxStatusAppearance(AppverseWebBoxStatusResources resources, BoxTemplate template) {
    super(resources, template);
  }

}
