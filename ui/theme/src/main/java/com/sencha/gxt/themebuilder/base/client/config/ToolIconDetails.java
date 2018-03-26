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
public interface ToolIconDetails {
  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.5)", comment = "primary color of icons")
  String primaryColor();

  @TypeDetails(sampleValue = "1", comment = "primary opacity of icons")
  double primaryOpacity();

  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.3)", comment = "icon color when hover")
  String primaryOverColor();

  @TypeDetails(sampleValue = "1", comment = "opacity when hover")
  double primaryOverOpacity();

  @TypeDetails(sampleValue = "util.mixColors('#ffffff', '#157FCC', 0.25)", comment = "icon color when clicked")
  String primaryClickColor();

  @TypeDetails(sampleValue = "1", comment = "icon opacity when clicked")
  double primaryClickOpacity();

  @TypeDetails(sampleValue = "#ff0000", comment = "color used for warning actions, such as exclamation icon")
  String warningColor();

  @TypeDetails(sampleValue = "#00ff00", comment = "color used for allowed actions, such as allowed drop zones with DnD")
  String allowColor();
}
