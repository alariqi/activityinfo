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

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.ResourceGeneratorType;
import com.sencha.gxt.themebuilder.base.rebind.DetailsResourceGenerator;

@ResourceGeneratorType(DetailsResourceGenerator.class)
public interface ThemeDetails extends ResourcePrototype {

  AccordionLayoutDetails accordionLayout();

  @TypeDetails(sampleValue = "#000000", comment = "Default color to use on borders in the theme")
  String borderColor();

  @TypeDetails(sampleValue = "#000000", comment = "Default color to use for backgrounds, usually within panels and the like")
  String backgroundColor();

  @TypeDetails(sampleValue = "0.6", comment = "Opactiy value to use on disabled elements/widgets")
  double disabledOpacity();

  @TypeDetails(sampleValue = "'gray'", comment = "Text color to use in disabled widgets. Can be left blank to not set a color and instead let widgets retain their defaults")
  String disabledTextColor();

  BorderLayoutDetails borderLayout();

  ButtonDetails button();

  ButtonGroupDetails buttonGroup();

  FieldDetails field();

  DatePickerDetails datePicker();

  PanelDetails panel();

  FramedPanelDetails framedPanel();

  MenuDetails menu();

  SplitBarDetails splitbar();

  WindowDetails window();

  TabDetails tabs();

  ToolBarDetails toolbar();

  ToolIconDetails tools();

  InfoDetails info();

  FieldSetDetails fieldset();

  TipDetails tip();

  TreeDetails tree();

  TipDetails errortip();

  GridDetails grid();

  ListViewDetails listview();

  StatusDetails status();

  MaskDetails mask();

  ProgressBarDetails progressbar();

  StatusProxyDetails statusproxy();

  ColorPaletteDetails colorpalette();

  MessageBoxDetails messagebox();

  ResizableDetails resizable();
}
