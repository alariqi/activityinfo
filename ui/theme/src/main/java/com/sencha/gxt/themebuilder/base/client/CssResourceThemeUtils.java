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
package com.sencha.gxt.themebuilder.base.client;

/**
 * This class is for operations that can be done in the css file and don't belong in the theme config. All methods
 * here should be able to compile to static content.
 */
public class CssResourceThemeUtils {
  /**
   * Takes an opacity value 0.0-1.0 and converts it to a ie alpha filter function string. Given a constant value,
   * this should compile out to a constant string.
   */
  public static String opacityToIe8Filter(double opacity) {
    //-ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=50)"
    return "alpha(opacity=" + ((int) (opacity * 100)) + ")";
  }
  /**
   * Takes two int values and returns the max of the two, with 'px' appended to the end. Given two constant values,
   * this should compile out to a constant string in the css.
   */
  public static String maxPxSize(int a, int b) {
    return Math.max(a, b) + "px";
  }

  /**
   * Calculates the padding size needed to make sliced/css3 look the same based on borderRadius and border size
   */
  public static String framePaddingCalc(int radius, int border) {
    return (Math.max(radius, border) - border) + "px";
  }

}
