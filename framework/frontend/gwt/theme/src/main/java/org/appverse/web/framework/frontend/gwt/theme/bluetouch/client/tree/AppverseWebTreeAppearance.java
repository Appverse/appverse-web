/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.sencha.gxt.theme.base.client.tree.TreeBaseAppearance;

public class AppverseWebTreeAppearance extends TreeBaseAppearance {

  public interface AppverseWebTreeResources extends TreeResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/tree/Tree.css", "TreeDefault.css"})
    TreeBaseStyle style();

  }

  public AppverseWebTreeAppearance() {
    super((TreeResources) GWT.create(AppverseWebTreeResources.class));
  }
}
