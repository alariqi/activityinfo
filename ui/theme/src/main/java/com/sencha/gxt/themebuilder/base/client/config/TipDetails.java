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

public interface TipDetails {

  @TypeDetails(sampleValue="#ffffff", comment = "background color")
  String backgroundColor();

  @TypeDetails(sampleValue = "1", comment = "tooltip opacity, 0.0-1.0")
  double opacity();

  @TypeDetails(sampleValue = "util.padding(2)", comment = "padding")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "6", comment = "border radius")
  int borderRadius();

  @TypeDetails(sampleValue = "util.border('solid', '#cccccc', 1)", comment = "border parameters")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.radiusMinusBorderWidth(border, borderRadius)", comment = "helper for leftover space in css3 versus sliced images")
  EdgeDetails radiusMinusBorderWidth();

  @TypeDetails(sampleValue = "util.margin(0)", comment = "margin")
  EdgeDetails margin();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "info header text style")
  FontDetails headerText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "info message text style")
  FontDetails messageText();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding around the header text")
  EdgeDetails headerPadding();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding around the message text")
  EdgeDetails messagePadding();
}
