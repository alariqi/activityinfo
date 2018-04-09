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


public class ThemeDetailsNoOpImpl implements ThemeDetails {
  private static final RuntimeException EXCEPTION = new IllegalStateException("NoOp implementation called");


  @Override
  public AccordionLayoutDetails accordionLayout() {
    throw EXCEPTION;
  }

  @Override
  public String borderColor() {
    throw EXCEPTION;

  }

  @Override
  public String backgroundColor() {
    throw EXCEPTION;

  }

  @Override
  public double disabledOpacity() {
    throw EXCEPTION;

  }

  @Override
  public String disabledTextColor() {
    throw EXCEPTION;

  }

  @Override
  public BorderLayoutDetails borderLayout() {
    throw EXCEPTION;

  }

  @Override
  public ButtonDetails button() {
    throw EXCEPTION;

  }

  @Override
  public ButtonGroupDetails buttonGroup() {
    throw EXCEPTION;
  }


  @Override
  public FieldDetails field() {
    throw EXCEPTION;

  }

  @Override
  public DatePickerDetails datePicker() {
    throw EXCEPTION;

  }

  @Override
  public PanelDetails panel() {
    throw EXCEPTION;

  }

  @Override
  public FramedPanelDetails framedPanel() {
    throw EXCEPTION;

  }

  @Override
  public MenuDetails menu() {
    throw EXCEPTION;

  }

  @Override
  public SplitBarDetails splitbar() {
    throw EXCEPTION;

  }

  @Override
  public WindowDetails window() {
    throw EXCEPTION;

  }

  @Override
  public TabDetails tabs() {
    throw EXCEPTION;

  }

  @Override
  public ToolBarDetails toolbar() {
    throw EXCEPTION;

  }

  @Override
  public ToolIconDetails tools() {

    throw EXCEPTION;
  }

  @Override
  public InfoDetails info() {
    throw EXCEPTION;

  }

  @Override
  public FieldSetDetails fieldset() {
    throw EXCEPTION;

  }

  @Override
  public TipDetails tip() {
    throw EXCEPTION;

  }

  @Override
  public TreeDetails tree() {
    throw EXCEPTION;

  }

  @Override
  public TipDetails errortip() {
    throw EXCEPTION;

  }

  @Override
  public GridDetails grid() {
    throw EXCEPTION;

  }

  @Override
  public ListViewDetails listview() {
    throw EXCEPTION;

  }

  @Override
  public StatusDetails status() {
    throw EXCEPTION;

  }

  @Override
  public MaskDetails mask() {
    throw EXCEPTION;

  }

  @Override
  public ProgressBarDetails progressbar() {
    throw EXCEPTION;

  }

  @Override
  public StatusProxyDetails statusproxy() {
    throw EXCEPTION;

  }

  @Override
  public ColorPaletteDetails colorpalette() {
    throw EXCEPTION;

  }

  @Override
  public MessageBoxDetails messagebox() {
    throw EXCEPTION;

  }

  @Override
  public ResizableDetails resizable() {
    throw EXCEPTION;
  }

  @Override
  public String getName() {
    throw EXCEPTION;
  }
}
