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

public interface ProgressBarDetails {

  @TypeDetails(sampleValue = "util.border('solid', '#000000', 1)", comment = "border around the entire progressbar")
  BorderDetails border();

  @TypeDetails(sampleValue = "util.solidGradientString('#ffffff')", comment = "background gradient for the empty progressbar")
  String backgroundGradient();

  @TypeDetails(sampleValue = "util.fontStyle(\"sans-serif\", 'medium', '#000000', 'bold')", comment = "font style for the progressbar text")
  FontDetails text();

  @TypeDetails(sampleValue = "'center'", comment = "alignment for the progressbar text, may be 'center', 'left', or 'right'")
  String textAlign();

  @TypeDetails(sampleValue = "util.padding(4, 0)", comment = "padding around the progressbar, typically with zero left/right padding to prevent offsetting the bar itself")
  EdgeDetails textPadding();

  @TypeDetails(sampleValue = "util.solidGradientString('#add8e6')", comment = "gradient for the progress bar as it fills")
  String barGradient();

  @TypeDetails(sampleValue = "util.border('solid', '#000000', 0, 1, 0, 0)", comment = "border around the progressbar as it fills")
  BorderDetails barBorder();

  @TypeDetails(sampleValue = "#000000", comment = "text color for the text covered by the progress bar as it fills")
  String barTextColor();
}
