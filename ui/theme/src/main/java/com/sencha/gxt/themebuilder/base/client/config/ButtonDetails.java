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

public interface ButtonDetails {

  @TypeDetails(sampleValue = "#ffffff", comment = "Color of the menu arrow")
  String arrowColor();

  @TypeDetails(sampleValue = "#4B9CD7", comment = "the normal state background color")
  String backgroundColor();

  @TypeDetails(sampleValue = "util.border('solid', '#126DAF', 1)", comment = "the buttons border")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.border('solid', '#126DAF', 1)", comment = "the buttons border when hovered")
  BorderDetails overBorder();

  @TypeDetails(sampleValue = "util.border('solid', '#126DAF', 1)", comment = "the buttons border when pressed")
  BorderDetails pressedBorder();

  @TypeDetails(sampleValue = "3", comment = "border radius of the button")
  int borderRadius();
  
  @TypeDetails(sampleValue = "util.radiusMinusBorderWidth(border, borderRadius)", comment = "helper for leftover space in css3 versus sliced images")
  EdgeDetails radiusMinusBorderWidth();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','12px','#FFFFFF')", comment = "the buttons text")
  FontDetails font();

  @TypeDetails(sampleValue = "#4B9CD7 0%, #3892D3 50%, #358AC8 51%, #3892D3", comment = "the normal state gradient")
  String gradient();

  @TypeDetails(sampleValue = "16", comment = "the large font size")
  String largeFontSize();

  @TypeDetails(sampleValue = "32", comment = "the large line height")
  String largeLineHeight();

  @TypeDetails(sampleValue = "14", comment = "the medium font size")
  String mediumFontSize();

  @TypeDetails(sampleValue = "24", comment = "the medium line height")
  String mediumLineHeight();

  @TypeDetails(sampleValue = "#4792C8", comment = "the mouseover state background color")
  String overBackgroundColor();

  @TypeDetails(sampleValue = "#4792C8, #3386C2 50%, #307FB8 51%, #3386C2", comment = "the mouseover state gradient")
  String overGradient();

  @TypeDetails(sampleValue = "util.padding(3)", comment = "the button's padding")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "#2A6D9E", comment = "the pressed state background color")
  String pressedBackgroundColor();

  @TypeDetails(sampleValue = "#2A6D9E, #276796 50%, #2A6D9E 51%, #3F7BA7", comment = "the pressed state gradient")
  String pressedGradient();

  @TypeDetails(sampleValue = "12", comment = "the small font size")
  String smallFontSize();

  @TypeDetails(sampleValue = "18", comment = "the small line height")
  String smallLineHeight();
}
