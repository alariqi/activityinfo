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
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;

public class Css3CheckBoxAppearance implements CheckBoxCell.CheckBoxAppearance {

  private final String type;

  public Css3CheckBoxAppearance() {
    this("checkbox");
  }

  protected Css3CheckBoxAppearance(String type) {
    this.type = type;
  }

  @Override
  public void render(SafeHtmlBuilder sb, Boolean value, CheckBoxCell.CheckBoxCellOptions options) {
    String checkBoxId = XDOM.getUniqueId();

    String nameParam = options.getName() != null ? " name='" + options.getName() + "' " : "";
    String disabledParam = options.isDisabled() ? " disabled=true" : "";
    String readOnlyParam = options.isReadonly() ? " readonly" : "";
    String idParam = " id=" + checkBoxId;
    String typeParam = " type=" + type;
    String checkedParam = value ? " checked" : "";

    sb.appendHtmlConstant("<fieldset class=\"fieldset__checkbox\">");
    sb.appendHtmlConstant("<input " + typeParam + nameParam + disabledParam + readOnlyParam + idParam + checkedParam + " />");
    sb.appendHtmlConstant("<label for=" + checkBoxId + " tabindex=\"0\">");
    if (options.getBoxLabel() != null) {
      sb.append(options.getBoxLabel());
    }
    sb.appendHtmlConstant("</label>");
    sb.appendHtmlConstant("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"40\" height=\"40\"" +
            " viewBox=\"0 0 21 17\" class=\"icon\" preserveAspectRatio=\"xMinYMin meet\">" +
            "<use xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"#checkmark\"></use></svg>");
    sb.appendHtmlConstant("</fieldset>");
  }

  @Override
  public void setBoxLabel(SafeHtml boxLabel, XElement parent) {
    parent.selectNode("label").<LabelElement> cast().setInnerSafeHtml(boxLabel);
  }

  @Override
  public XElement getInputElement(Element parent) {
    return parent.<XElement> cast().selectNode("input");
  }

  @Override
  public void setReadOnly(Element parent, boolean readOnly) {
    getInputElement(parent).<InputElement> cast().setReadOnly(readOnly);
  }

  @Override
  public void onEmpty(Element parent, boolean empty) {
  }

  @Override
  public void onFocus(Element parent, boolean focus) {
    // Override method to prevent outline from being applied to check boxes on
    // focus
  }

  @Override
  public void onValid(Element parent, boolean valid) {
    // no-op, cb is true or false...
  }
}
