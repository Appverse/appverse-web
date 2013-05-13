/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.toolbar.PagingToolBarBaseAppearance;

public class DefaultPagingToolBarAppearance extends PagingToolBarBaseAppearance {
  public interface BluePagingToolBarResources extends PagingToolBarResources, ClientBundle {
    ImageResource first();

    ImageResource prev();

    ImageResource next();

    ImageResource last();

    ImageResource refresh();

    ImageResource loading();
  }

  public DefaultPagingToolBarAppearance() {
    this(GWT.<BluePagingToolBarResources> create(BluePagingToolBarResources.class));
  }

  public DefaultPagingToolBarAppearance(BluePagingToolBarResources resources) {
    super(resources);
  }
}
