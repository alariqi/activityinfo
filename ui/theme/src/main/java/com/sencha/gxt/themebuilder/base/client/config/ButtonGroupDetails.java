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

public interface ButtonGroupDetails {

  @TypeDetails(sampleValue = "#FFFFFF", comment = "the body background color")
  String bodyBackgroundColor();

  @TypeDetails(sampleValue = "util.padding(4)", comment = "the group's body padding")
  EdgeDetails bodyPadding();

  @TypeDetails(sampleValue = "util.border('solid', '#dfeaf2', 3)", comment = "the groups border")
  BorderDetails border();

  @TypeDetails(sampleValue = "3", comment = "border radius of the button group")
  int borderRadius();

  @TypeDetails(sampleValue = "util.fontStyle('helvetica, arial, verdana, sans-serif','13px','#666666')", comment = "the groups text")
  FontDetails font();

  @TypeDetails(sampleValue = "#4B9CD7 0%, #3892D3 50%, #358AC8 51%, #3892D3", comment = "the header gradient")
  String headerGradient();

  @TypeDetails(sampleValue = "util.padding(2)", comment = "the group's header padding")
  EdgeDetails headerPadding();
}
