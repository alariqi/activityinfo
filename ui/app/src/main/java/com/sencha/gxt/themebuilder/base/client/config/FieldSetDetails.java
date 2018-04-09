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

public interface FieldSetDetails {
  @TypeDetails(sampleValue = "#ffffff", comment = "Background color")
  String backgroundColor();

  @TypeDetails(sampleValue = "util.padding(5)", comment = "padding")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "util.padding(5)", comment = "legend padding")
  EdgeDetails legendPadding();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','large')", comment = "text details for the fieldset's legend")
  FontDetails text();

  @TypeDetails(sampleValue = "util.border('solid', '#bbbbbb', 1)", comment = "border styling and colors around the fieldset")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.5)", comment = "primary color of collapse icon")
  String collapseIconColor();

  @TypeDetails(sampleValue = "#dddddd", comment = "collapse icon color when hovered")
  String collapseOverIconColor();

  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.5)", comment = "primary color of expand icon")
  String expandIconColor();

  @TypeDetails(sampleValue = "#dddddd", comment = "expand icon color when hovered")
  String expandOverIconColor();
}
