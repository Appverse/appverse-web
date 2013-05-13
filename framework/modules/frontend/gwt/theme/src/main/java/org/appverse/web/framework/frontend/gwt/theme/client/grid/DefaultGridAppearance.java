/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.grid;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.grid.GridBaseAppearance;

public class DefaultGridAppearance extends GridBaseAppearance {

  public interface BlueGridStyle extends GridStyle {
    
  }
  
  public interface BlueGridResources extends GridResources  {
    
    @Source({"com/sencha/gxt/theme/base/client/grid/Grid.css", "BlueGrid.css"})
    @Override
    BlueGridStyle css();
  }
  
  
  public DefaultGridAppearance() {
    this(GWT.<BlueGridResources> create(BlueGridResources.class));
  }

  public DefaultGridAppearance(BlueGridResources resources) {
    super(resources);
  }
}
