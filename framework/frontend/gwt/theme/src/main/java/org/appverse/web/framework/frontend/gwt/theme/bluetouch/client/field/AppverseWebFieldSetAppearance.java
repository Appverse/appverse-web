/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.field;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.field.FieldSetDefaultAppearance;

public class AppverseWebFieldSetAppearance extends FieldSetDefaultAppearance {

  public interface AppverseWebFieldSetResources extends FieldSetResources {
    
    @Override
    @Source({"com/sencha/gxt/theme/base/client/field/FieldSet.css", "AppverseWebFieldSet.css"})
    public AppverseWebFieldSetStyle css();
  }
  
  public interface AppverseWebFieldSetStyle extends FieldSetStyle {
    
  }
  
  
  public AppverseWebFieldSetAppearance() {
    this(GWT.<AppverseWebFieldSetResources>create(AppverseWebFieldSetResources.class));
  }
  
  public AppverseWebFieldSetAppearance(AppverseWebFieldSetResources resources) {
    super(resources);
  }
  
}
