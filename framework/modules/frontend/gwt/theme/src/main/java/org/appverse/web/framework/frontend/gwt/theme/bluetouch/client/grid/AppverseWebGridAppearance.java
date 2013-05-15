/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.grid;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.grid.GridBaseAppearance;

public class AppverseWebGridAppearance extends GridBaseAppearance {

  public interface AppverseWebGridStyle extends GridStyle {
    
  }
  
  public interface AppverseWebGridResources extends GridResources  {
    
    @Source({"com/sencha/gxt/theme/base/client/grid/Grid.css", "AppverseWebGrid.css"})
    @Override
    AppverseWebGridStyle css();
  }
  
  
  public AppverseWebGridAppearance() {
    this(GWT.<AppverseWebGridResources> create(AppverseWebGridResources.class));
  }

  public AppverseWebGridAppearance(AppverseWebGridResources resources) {
    super(resources);
  }
}
