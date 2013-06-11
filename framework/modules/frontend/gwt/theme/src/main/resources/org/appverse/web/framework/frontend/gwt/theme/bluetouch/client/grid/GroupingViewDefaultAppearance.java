/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.theme.base.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.grid.GroupingView.GroupingData;
import com.sencha.gxt.widget.core.client.grid.GroupingView.GroupingViewAppearance;

public class GroupingViewDefaultAppearance implements GroupingViewAppearance {

  public interface DefaultHeaderTemplate extends XTemplates, GroupHeaderTemplate<Object> {
    @Override
    @XTemplate("{groupInfo.value}")
    public SafeHtml renderGroupHeader(GroupingData<Object> groupInfo);
  }

  public interface GroupHeaderTemplate<M> {
    SafeHtml renderGroupHeader(GroupingData<M> groupInfo);
  }

  public interface GroupingViewResources extends ClientBundle {

    ImageResource groupBy();
    
    @Source("GroupingView.css")
    GroupingView.GroupingViewStyle style();
  }

  public interface GroupTemplate<M> {
    SafeHtml renderGroup(GroupingView.GroupingViewStyle style, SafeHtml groupHeader, SafeHtml rows, SafeHtml groupSummary);
  }

  private final GroupingViewResources resources;
  private GroupingView.GroupingViewStyle style;
  private GroupHeaderTemplate<?> headerTemplate = GWT.create(DefaultHeaderTemplate.class);

  public GroupingViewDefaultAppearance() {
    this(GWT.<GroupingViewResources> create(GroupingViewResources.class));
  }

  public GroupingViewDefaultAppearance(GroupingViewResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public XElement findHead(XElement element) {
    return element.findParent("." + style.gridGroupHead(), 10);
  }

  @Override
  public XElement getGroup(XElement head) {
    return head.getParentElement().cast();
  }

  @Override
  public ImageResource groupByIcon() {
    return resources.groupBy();
  }

  @Override
  public boolean isCollapsed(XElement group) {
    return group.hasClassName(style.gridGroupCollapsed());
  }

  @Override
  public void onGroupExpand(XElement group, boolean expanded) {
    group.setClassName(style.gridGroupCollapsed(), !expanded);
    if (GXT.isIE7()) {
      group.getNextSiblingElement().getStyle().setDisplay(expanded ? Display.BLOCK : Display.NONE);
    } else {
      group.getNextSiblingElement().getStyle().setProperty("display", expanded ? "table-row" : "none");
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public SafeHtml renderGroupHeader(GroupingData<?> groupInfo) {
    return headerTemplate.renderGroupHeader((GroupingData) groupInfo);
  }

  public void setHeaderTemplate(GroupHeaderTemplate<?> headerTemplate) {
    this.headerTemplate = headerTemplate;
  }

  @Override
  public GroupingView.GroupingViewStyle style() {
    return style;
  }

}
