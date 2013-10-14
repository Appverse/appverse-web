/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.progress;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.progress.ProgressBarDefaultAppearance;

public class AppverseWebProgressBarAppearance extends ProgressBarDefaultAppearance {

  public interface AppverseWebProgressBarResources extends ProgressBarResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/progress/ProgressBar.css", "ProgressBar.css"})
    @Override
    ProgressBarStyle style();

    @Source("progress-bg.gif")
    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bar();
    
    @Source("bg.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource innerBar();
  }

  public AppverseWebProgressBarAppearance() {
    super(GWT.<ProgressBarResources> create(AppverseWebProgressBarResources.class),
        GWT.<ProgressBarTemplate> create(ProgressBarTemplate.class));
  }

}
