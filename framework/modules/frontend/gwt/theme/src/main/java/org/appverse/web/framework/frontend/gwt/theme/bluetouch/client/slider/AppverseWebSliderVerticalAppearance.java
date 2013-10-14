/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.bluetouch.client.slider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.cell.core.client.SliderCell.VerticalSliderAppearance;
import com.sencha.gxt.theme.base.client.slider.SliderVerticalBaseAppearance;

public class AppverseWebSliderVerticalAppearance extends SliderVerticalBaseAppearance implements VerticalSliderAppearance {

  public static class AppverseWebSliderVerticalAppearanceHelper {

    public static String getTrackVerticalBottom() {
      return new StringBuilder("url(").append(GWT.getModuleBaseURL()).append(
          "/bluetouch/images/slider/trackVerticalBottom.png);").toString();
    }

    public static String getTrackVerticalMiddle() {
      return new StringBuilder("url(").append(GWT.getModuleBaseURL()).append(
          "/bluetouch/images/slider/trackVerticalMiddle.png);").toString();
    }

    public static String getTrackVerticalTop() {
      return new StringBuilder("url(").append(GWT.getModuleBaseURL()).append(
          "/bluetouch/images/slider/trackVerticalTop.png);").toString();
    }

  }

  public interface AppverseWebSliderVerticalResources extends SliderVerticalResources, ClientBundle {

    @Source({
        "com/sencha/gxt/theme/base/client/slider/Slider.css",
        "com/sencha/gxt/theme/base/client/slider/SliderVertical.css", "AppverseWebSliderVertical.css"})
    AppverseWebVerticalSliderStyle style();

    ImageResource thumbVertical();

    ImageResource thumbVerticalDown();

    ImageResource thumbVerticalOver();

    ImageResource trackVerticalBottom();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource trackVerticalMiddle();

    ImageResource trackVerticalTop();

  }

  public interface AppverseWebVerticalSliderStyle extends BaseSliderVerticalStyle, CssResource {
  }

  public AppverseWebSliderVerticalAppearance() {
    this(GWT.<AppverseWebSliderVerticalResources> create(AppverseWebSliderVerticalResources.class),
        GWT.<SliderVerticalTemplate> create(SliderVerticalTemplate.class));
  }

  public AppverseWebSliderVerticalAppearance(AppverseWebSliderVerticalResources resources, SliderVerticalTemplate template) {
    super(resources, template);
  }

}
