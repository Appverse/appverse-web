/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.field;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.field.FieldSetDefaultAppearance;

public class DefaultFieldSetAppearance extends FieldSetDefaultAppearance {

  public interface BlueFieldSetResources extends FieldSetResources {
    
    @Override
    @Source({"com/sencha/gxt/theme/base/client/field/FieldSet.css", "BlueFieldSet.css"})
    public BlueFieldSetStyle css();
  }
  
  public interface BlueFieldSetStyle extends FieldSetStyle {
    
  }
  
  
  public DefaultFieldSetAppearance() {
    this(GWT.<BlueFieldSetResources>create(BlueFieldSetResources.class));
  }
  
  public DefaultFieldSetAppearance(BlueFieldSetResources resources) {
    super(resources);
  }
  
}
