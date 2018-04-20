/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package org.activityinfo.ui.client.base.slider;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.SliderCell.VerticalSliderAppearance;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.themebuilder.base.client.config.FieldDetails;
import com.sencha.gxt.themebuilder.base.client.config.SliderDetails;

/**
 *
 */
public class Css3VerticalSliderAppearance extends Css3HorizontalSliderAppearance implements VerticalSliderAppearance {
  public interface Css3VerticalSliderResources extends Css3HorizontalSliderResources {
    @Source({"Css3HorizontalSlider.gss", "Css3VerticalSlider.gss"})
    Css3VerticalSliderStyle style();
  }

  public interface Css3VerticalSliderStyle extends Css3HorizontalSliderStyle {
  }

  private final Css3VerticalSliderResources resources;

  public Css3VerticalSliderAppearance() {
    this(GWT.<Css3VerticalSliderResources>create(Css3VerticalSliderResources.class));
  }

  public Css3VerticalSliderAppearance(Css3VerticalSliderResources resources) {
    this(resources, GWT.<Css3HorizontalSliderTemplate>create(Css3HorizontalSliderTemplate.class));
  }

  public Css3VerticalSliderAppearance(Css3VerticalSliderResources resources, Css3HorizontalSliderTemplate template) {
    super(resources, template);
    this.resources = resources;
  }

  @Override
  public int getClickedValue(Context context, Element parent, Point location) {
    XElement track = getTrack(parent);
    return location.getY() - track.getTop(false) - (resources.theme().field().slider().thumbWidth() / 2);
  }

  @Override
  public int getSliderLength(XElement parent) {
    SliderDetails sliderDetails = resources.theme().field().slider();
    return getTrack(parent).getOffsetHeight() - sliderDetails.thumbWidth();
  }

  @Override
  public boolean isVertical() {
    return true;
  }

  @Override
  public void render(double fractionalValue, int width, int height, SafeHtmlBuilder sb) {
    if (height == -1) {
      // default
      height = 200;
    }

    FieldDetails fieldDetails = resources.theme().field();

    int thumbWidth = fieldDetails.slider().thumbWidth();
    int thumbHeight = fieldDetails.slider().thumbHeight();

    int fieldWidth = Math.max(fieldDetails.height(), fieldDetails.slider().thumbHeight());
    fieldWidth = Math.max(fieldWidth, fieldDetails.slider().trackHeight());

    int halfThumbWidth = thumbWidth / 2;
    int maxTrackLength = height - thumbWidth;

    int offset = (int) (fractionalValue * maxTrackLength) - halfThumbWidth;
    offset = Math.max(-halfThumbWidth, offset);
    offset = Math.min(maxTrackLength + halfThumbWidth, offset);

    SafeStylesBuilder sliderStyleBuilder = new SafeStylesBuilder();
    sliderStyleBuilder.appendTrustedString("height:" + height + "px;");
    sliderStyleBuilder.appendTrustedString("width:" + fieldWidth + "px;");


    SafeStylesBuilder trackStyleBuilder = new SafeStylesBuilder();
    trackStyleBuilder.appendTrustedString("height: " + height + "px;");
    trackStyleBuilder.appendTrustedString("left:" + ((fieldWidth - fieldDetails.slider().trackHeight()) / 2) + "px;");

    SafeStylesBuilder thumbStyleBuilder = new SafeStylesBuilder();
    thumbStyleBuilder.appendTrustedString("bottom:" + offset + "px;");
    // reversed width/height due to orientation
    int thumbLeft = ((fieldDetails.slider().trackHeight() - thumbHeight) / 2)
        - (fieldDetails.slider().trackBorder().top() + fieldDetails.slider().trackBorder().bottom());
    thumbStyleBuilder.appendTrustedString("left:" + thumbLeft + "px;");
    thumbStyleBuilder.appendTrustedString("margin-bottom:" + halfThumbWidth + "px;");

    SafeStylesBuilder thumbCenterStyleBuilder = new SafeStylesBuilder();
    updateThumbCenterStyle(thumbCenterStyleBuilder, "top", "height", thumbWidth,
        fieldDetails.slider().thumbBorder().left(), fieldDetails.slider().thumbBorder().right());
    updateThumbCenterStyle(thumbCenterStyleBuilder, "left", "width", thumbHeight,
        fieldDetails.slider().thumbBorder().top(), fieldDetails.slider().thumbBorder().bottom());


    sb.append(template.render(resources.style(), sliderStyleBuilder.toSafeStyles(),
        trackStyleBuilder.toSafeStyles(), thumbStyleBuilder.toSafeStyles(), thumbCenterStyleBuilder.toSafeStyles()));
  }

  @Override
  public void setThumbPosition(Element parent, int pos) {
    XElement thumbElement = XElement.as(getThumb(parent));
    int halfThumbSize = resources.theme().field().slider().thumbWidth() / 2;
    pos = Math.max(-halfThumbSize, pos);
    thumbElement.getStyle().setBottom(pos, Unit.PX);
  }
}
