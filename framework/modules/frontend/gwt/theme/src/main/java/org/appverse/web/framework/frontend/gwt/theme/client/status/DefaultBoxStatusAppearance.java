/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.status;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.status.BoxStatusBaseAppearance;
import com.sencha.gxt.widget.core.client.Status.BoxStatusAppearance;

public class DefaultBoxStatusAppearance extends BoxStatusBaseAppearance implements BoxStatusAppearance {

  public interface BlueBoxStatusStyle extends BoxStatusStyle {

    String status();

    String statusIcon();

    String statusText();

    String statusBox();

  }

  public interface BlueBoxStatusResources extends BoxStatusResources, ClientBundle {

    @Override
    @Source({"com/sencha/gxt/theme/base/client/status/Status.css", "BlueBoxStatus.css"})
    BlueBoxStatusStyle style();

    @Override
    @Source("com/sencha/gxt/theme/base/client/grid/loading.gif")
    ImageResource loading();

  }

  public DefaultBoxStatusAppearance() {
    this(GWT.<BlueBoxStatusResources> create(BlueBoxStatusResources.class), GWT.<BoxTemplate> create(BoxTemplate.class));
  }

  public DefaultBoxStatusAppearance(BlueBoxStatusResources resources, BoxTemplate template) {
    super(resources, template);
  }

}
