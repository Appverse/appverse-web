/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.container;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.container.BorderLayoutBaseAppearance;

public class DefaultBorderLayoutAppearance extends BorderLayoutBaseAppearance {

  public interface BlueBorderLayoutResources extends BorderLayoutResources {
    @Override
    @Source({"com/sencha/gxt/theme/base/client/container/BorderLayout.css", "BlueBorderLayout.css"})
    public BlueBorderLayoutStyle css();
  }

  public interface BlueBorderLayoutStyle extends BorderLayoutStyle {

  }

  public DefaultBorderLayoutAppearance() {
    super(GWT.<BlueBorderLayoutResources> create(BlueBorderLayoutResources.class));
  }

}
