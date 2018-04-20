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
package org.activityinfo.ui.client.base.field;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.cell.core.client.form.ValueBaseInputCell.ValueBaseFieldAppearance;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.form.Field.FieldStyles;

public abstract class Css3ValueBaseFieldAppearance implements ValueBaseFieldAppearance {

  public interface Css3ValueBaseFieldResources {
    Css3ValueBaseFieldStyle style();

    ThemeDetails theme();
  }

  public interface Css3ValueBaseFieldStyle extends CssResource, FieldStyles {
    @Override
    String focus();

    @Override
    String invalid();

    String empty();

    String field();

    String readonly();

    String wrap();

  }

  private final Css3ValueBaseFieldStyle style;

  public Css3ValueBaseFieldAppearance(Css3ValueBaseFieldResources resources) {
    this.style = resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }

  @Override
  public void onEmpty(Element parent, boolean empty) {
    getInputElement(parent).setClassName(style.empty(), empty);
  }

  @Override
  public void onFocus(Element parent, boolean focus) {
    parent.<XElement>cast().setClassName(style.focus(), focus);
  }

  @Override
  public void onValid(Element parent, boolean valid) {
    parent.<XElement>cast().setClassName(style.invalid(), !valid);
  }

  @Override
  public void setReadOnly(Element parent, boolean readOnly) {
    getInputElement(parent).<InputElement>cast().setReadOnly(readOnly);
    getInputElement(parent).setClassName(style.readonly(), readOnly);
  }
}
