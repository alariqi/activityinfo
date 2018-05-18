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

import com.google.common.base.Strings;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.FieldCell.FieldAppearanceOptions;
import com.sencha.gxt.cell.core.client.form.TextInputCell.TextFieldAppearance;
import com.sencha.gxt.core.client.dom.XElement;

public class Css3TextFieldAppearance extends Css3ValueBaseFieldAppearance implements TextFieldAppearance {

    private String units;

    public Css3TextFieldAppearance() {
        super();
    }

    /**
     *
     * @param units a string that appears at the end of the input field.
     */
    public Css3TextFieldAppearance(String units) {
        this.units = units;
    }

    @Override
    public XElement getInputElement(Element parent) {
        return parent.getFirstChildElement().getFirstChildElement().cast();
    }

    @Override
    public void onResize(XElement parent, int width, int height) {
    }

    @Override
    public void render(SafeHtmlBuilder sb, String type, String value, FieldAppearanceOptions options) {
        sb.appendHtmlConstant("<div class='field__wrap'>");
        renderInput(sb, value, options);
        if(!Strings.isNullOrEmpty(units)) {
            sb.appendHtmlConstant("<div class=\"field__units\">");
            sb.appendEscaped(units);
            sb.appendHtmlConstant("</span>");
        }
        sb.appendHtmlConstant("</div>");
    }
}
