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
package com.sencha.gxt.themebuilder.base.client.config;

public interface MaskDetails {

  @TypeDetails(sampleValue = "0.5", comment = "opacity of the element that covers masked content")
  double opacity();

  @TypeDetails(sampleValue = "#000000", comment = "color of the element that covers masked content")
  String backgroundColor();

  BoxDetails box();
  public interface BoxDetails {

    @TypeDetails(sampleValue = "util.padding(5)", comment = "padding between the border and the content of the mask message")
    EdgeDetails padding();

    @TypeDetails(sampleValue = "#555555", comment = "border color for the mask's message")
    String borderColor();

    @TypeDetails(sampleValue = "'solid'", comment = "border style for the mask's message")
    String borderStyle();

    @TypeDetails(sampleValue = "2", comment = "border width for the mask's message")
    int borderWidth();

    @TypeDetails(sampleValue = "0", comment = "border radius for the mask's message")
    int borderRadius();

    @TypeDetails(sampleValue = "util.max(0, borderRadius - borderWidth)", comment = "helper for leftover space in css3 versus sliced images")
    int radiusMinusBorderWidth();


    @TypeDetails(sampleValue = "#dddddd", comment = "background color for the mask's message")
    String backgroundColor();

    @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'normal', '#555555')", comment = "font style for mask text")
    FontDetails text();

    @TypeDetails(sampleValue = "util.padding(21, 0, 0)", comment = "padding around the text, useful to provide space for loading image")
    EdgeDetails textPadding();

    @TypeDetails(sampleValue = "'center 0'", comment = "css background-position for the loading image, if any")
    String loadingImagePosition();
  }

}
