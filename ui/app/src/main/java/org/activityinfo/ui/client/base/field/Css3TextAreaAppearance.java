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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.cell.core.client.form.FieldCell.FieldAppearanceOptions;
import com.sencha.gxt.cell.core.client.form.TextAreaInputCell.TextAreaAppearance;
import com.sencha.gxt.cell.core.client.form.TextAreaInputCell.TextAreaCellOptions;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Size;
import org.activityinfo.ui.client.base.field.Css3TextFieldAppearance.Css3TextFieldStyle;

/**
 *
 */
public class Css3TextAreaAppearance extends Css3ValueBaseFieldAppearance implements TextAreaAppearance {

  public interface Css3TextAreaResources extends Css3ValueBaseFieldResources, ClientBundle {
    @Source({"Css3ValueBaseField.gss", "Css3TextField.gss", "Css3TextArea.gss"})
    Css3TextAreaStyle style();
  }

  public interface Css3TextAreaStyle extends Css3TextFieldStyle {
    String area();
  }

  private final Css3TextAreaStyle style;

  public Css3TextAreaAppearance() {
    this(GWT.<Css3TextAreaResources>create(Css3TextAreaResources.class));
  }

  public Css3TextAreaAppearance(Css3TextAreaResources resources) {
    super(resources);

    this.style = resources.style();
  }

  @Override
  public XElement getInputElement(Element parent) {
    return parent.getFirstChildElement().getFirstChildElement().cast();
  }

  @Override
  public void onResize(XElement parent, int width, int height) {
    Element frame = parent.getFirstChildElement();
    XElement inputElement = getInputElement(parent);

    Size adj = adjustTextAreaSize(width, height);

    if (width != -1) {
      frame.getStyle().setWidth(width, Unit.PX);
      width = adj.getWidth();
      inputElement.getStyle().setWidth(width, Unit.PX);
    }

    if (height != -1) {
      frame.getStyle().setHeight(height, Unit.PX);
      height = adj.getHeight();
      inputElement.getStyle().setHeight(height, Unit.PX);
    }
  }

  @Override
  public void render(SafeHtmlBuilder sb, String value, FieldAppearanceOptions options) {
    String inputStyles = "";
    String wrapStyles = "";

    int width = options.getWidth();
    int height = options.getHeight();

    String name = options.getName() != null ? "name='" + options.getName() + "'" : "";
    String disabled = options.isDisabled() ? "disabled=true" : "";
    String placeholder = options.getEmptyText() != null ? " placeholder='" + SafeHtmlUtils.htmlEscape(options.getEmptyText()) + "' " : "";

    boolean empty = false;

    if ((value == null || value.equals("")) && options.getEmptyText() != null) {
      if (GXT.isIE8() || GXT.isIE9()) {
        value = options.getEmptyText();
      }
      empty = true;
    }


    Size adjusted = adjustTextAreaSize(width, height);

    if (width != -1) {
      wrapStyles += "width:" + width + "px;";
      width = adjusted.getWidth();
      inputStyles += "width:" + width + "px;";
    }

    if (height != -1) {
      height = adjusted.getHeight();
      inputStyles += "height: " + height + "px;";
    }

    String cls = style.area() + " " + style.field();
    if (empty) {
      cls += " " + style.empty();
    }

    String ro = options.isReadonly() ? " readonly" : "";

    if (options instanceof TextAreaCellOptions) {
      TextAreaCellOptions opts = (TextAreaCellOptions) options;
      inputStyles += "resize:" + opts.getResizable().name().toLowerCase() + ";";
    }


    sb.appendHtmlConstant("<div style='" + wrapStyles + "' class='" + style.wrap() + "'>");
    sb.appendHtmlConstant("<textarea " + name + disabled + " style='" + inputStyles + "' type='text' class='" + cls
        + "'" + ro + placeholder + ">");
    sb.append(SafeHtmlUtils.fromString(value));
    sb.appendHtmlConstant("</textarea></div>");
  }

  protected Size adjustTextAreaSize(int width, int height) {
    width = Math.max(0, width);
    height = Math.max(0, height);
    return new Size(width, height);
  }
}
