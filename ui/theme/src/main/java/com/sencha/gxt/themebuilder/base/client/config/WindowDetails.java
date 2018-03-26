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

public interface WindowDetails extends FramedPanelDetails {
  @Override
  @TypeDetails(sampleValue = "1", comment = "border radius of the framed panel")
  int borderRadius();

  @Override
  @TypeDetails(sampleValue = "util.radiusMinusBorderWidth(border, borderRadius)", comment = "helper for leftover space in css3 versus sliced images")
  EdgeDetails radiusMinusBorderWidth();


  @Override
  @TypeDetails(sampleValue = "#ffffff", comment = "background color for the panel body")
  String backgroundColor();

  @Override
  @TypeDetails(sampleValue = "util.padding(0)", comment = "entire panel padding")
  EdgeDetails padding();

  @Override
  @TypeDetails(sampleValue = "util.padding(10)", comment = "header padding")
  EdgeDetails headerPadding();

  @Override
  @TypeDetails(sampleValue = "#ccffff", comment = "background color to fill behind the header gradient")
  String headerBackgroundColor();

  @Override
  @TypeDetails(sampleValue = "#ccffff, #ccffff", comment = "header gradient string")
  String headerGradient();

  @Override
  @TypeDetails(sampleValue = "15px", comment = "header line height")
  String headerLineHeight();

  @Override
  @TypeDetails(sampleValue = "util.border('solid', '#000000', 1)", comment = "border around the contentpanel")
  BorderDetails border();

  @Override
  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium')", comment = "panel heading text style")
  FontDetails font();
}
