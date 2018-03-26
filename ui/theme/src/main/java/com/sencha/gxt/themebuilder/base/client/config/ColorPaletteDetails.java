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

public interface ColorPaletteDetails {
  @TypeDetails(sampleValue = "14", comment = "height and width of each color swatch")
  int itemSize();

  @TypeDetails(sampleValue = "util.padding(3)", comment = "padding between each color swatch")
  EdgeDetails itemPadding();

  @TypeDetails(sampleValue = "#ffffff", comment = "background color behind all of the items")
  String backgroundColor();

  @TypeDetails(sampleValue = "util.border('solid', '#e1e1e1', 1)", comment = "border around each color swatch")
  BorderDetails itemBorder();

  @TypeDetails(sampleValue = "#e6e6e6", comment = "background to surround a selected or hovered color swatch")
  String selectedBackgroundColor();

  @TypeDetails(sampleValue = "util.border('solid', '#666666', 1)", comment = "border to draw around a selected or hovered color swatch")
  BorderDetails selectedBorder();
}
