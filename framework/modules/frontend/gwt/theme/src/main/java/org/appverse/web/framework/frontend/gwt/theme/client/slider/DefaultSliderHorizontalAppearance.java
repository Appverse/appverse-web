/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.appverse.web.framework.frontend.gwt.theme.client.slider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.cell.core.client.SliderCell.HorizontalSliderAppearance;
import com.sencha.gxt.theme.base.client.slider.SliderHorizontalBaseAppearance;

public class DefaultSliderHorizontalAppearance extends SliderHorizontalBaseAppearance implements HorizontalSliderAppearance {

  public interface BlueHorizontalSliderStyle extends SliderHorizontalStyle, CssResource {
  }

  public static class BlueSliderHorizontalAppearanceHelper {

    public static String getTrackHorizontalLeft() {
      return new StringBuilder("url(").append(GWT.getModuleBaseURL()).append(
          "/blue/images/slider/trackHorizontalLeft.png);").toString();
    }

    public static String getTrackHorizontalMiddle() {
      return new StringBuilder("url(").append(GWT.getModuleBaseURL()).append(
          "blue/images/slider/trackHorizontalMiddle.png);").toString();
    }

    public static String getTrackHorizontalRight() {
      return new StringBuilder("url(").append(GWT.getModuleBaseURL()).append(
          "blue/images/slider/trackHorizontalRight.png);").toString();
    }

  }

  public interface BlueSliderHorizontalResources extends SliderHorizontalResources, ClientBundle {

    @Source({
        "com/sencha/gxt/theme/base/client/slider/Slider.css",
        "com/sencha/gxt/theme/base/client/slider/SliderHorizontal.css", "BlueSliderHorizontal.css"})
    BlueHorizontalSliderStyle style();

    ImageResource thumbHorizontal();

    ImageResource thumbHorizontalDown();

    ImageResource thumbHorizontalOver();

    ImageResource trackHorizontalLeft();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource trackHorizontalMiddle();

    ImageResource trackHorizontalRight();

  }

  public DefaultSliderHorizontalAppearance() {
    this(GWT.<BlueSliderHorizontalResources> create(BlueSliderHorizontalResources.class),
        GWT.<SliderHorizontalTemplate> create(SliderHorizontalTemplate.class));
  }

  public DefaultSliderHorizontalAppearance(BlueSliderHorizontalResources resources,
      SliderHorizontalTemplate template) {
    super(resources, template);
  }

}
