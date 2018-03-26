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

/**
 *
 */
public interface ToolBarDetails {
  @TypeDetails(sampleValue = "#ffffff", comment = "background color of the toolbar for browsers that dont support gradients")
  String backgroundColor();

  @TypeDetails(sampleValue = "util.solidGradientString('#ffffff')", comment = "background gradient of the toolbar")
  String gradient();

  @TypeDetails(sampleValue = "util.border('none')", comment = "border around the toolbar")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.border('none')", comment = "border around the separator")
  BorderDetails separatorBorder();

  @TypeDetails(sampleValue = "14", comment = "height of the separator")
  int separatorHeight();

  @TypeDetails(sampleValue = "util.padding(2)", comment = "padding between the toolbar's border and its contents")
  EdgeDetails padding();

  ButtonDetails buttonOverride();

  LabelToolItemDetails labelItem();

  public interface LabelToolItemDetails {
    @TypeDetails(sampleValue = "util.fontStyle('sans-serif', 'medium')", comment = "LabelToolItem text styling")
    FontDetails text();
    @TypeDetails(sampleValue = "'medium'", comment = "LabelToolItem text styling")
    String lineHeight();
    @TypeDetails(sampleValue = "util.padding(2, 2, 0)", comment = "label padding")
    EdgeDetails padding();

  }
}
