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

public interface ListViewDetails {

  public interface ItemDetails {

    @TypeDetails(sampleValue = "'#ffffff'", comment = "item background color for non-gradient supporting browsers")
    String backgroundColor();

    @Optional(defaultValue = "#000000", comment = "item text color")
    String color();

    @TypeDetails(sampleValue = "util.solidGradientString('#ffffff')", comment = "")
    String gradient();

    @TypeDetails(sampleValue = "util.border('none')", comment = "")
    BorderDetails border();

    @TypeDetails(sampleValue = "util.padding(0, 4)", comment = "")
    EdgeDetails padding();
  }

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'medium')", comment = "listview text styling")
  FontDetails text();
  @TypeDetails(sampleValue = "'normal'", comment = "height of text in listview")
  String lineHeight();

  @TypeDetails(sampleValue = "'#ffffff'", comment = "listview background color")
  String backgroundColor();

  @TypeDetails(sampleValue = "util.border('solid', '#444444', 1)", comment = "listview body border")
  BorderDetails border();

  ItemDetails item();
  ItemDetails overItem();
  ItemDetails selectedItem();

}
