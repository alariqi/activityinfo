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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.cell.core.client.form.ValueBaseInputCell.ValueBaseFieldAppearance;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;

public abstract class Css3ValueBaseFieldAppearance implements ValueBaseFieldAppearance {


    public Css3ValueBaseFieldAppearance() {
    }

    @Override
    public void onEmpty(Element parent, boolean empty) {
    }

    @Override
    public void onFocus(Element parent, boolean focus) {
        getWrapper(parent).setClassName("field--focused", focus);
    }

    @Override
    public void onValid(Element parent, boolean valid) {
        parent.<XElement>cast().setClassName("field--invalid", !valid);
    }

    @Override
    public void setReadOnly(Element parent, boolean readOnly) {
        getInputElement(parent).<InputElement>cast().setReadOnly(readOnly);
        getInputElement(parent).setClassName("field--readonly", readOnly);
    }

    protected final XElement getWrapper(Element parent) {
        return parent.getFirstChildElement().cast();
    }

    protected final void renderInput(SafeHtmlBuilder shb, String value, FieldCell.FieldAppearanceOptions options) {
        StringBuilder sb = new StringBuilder();
        sb.append("<input ");

        if (options.isDisabled()) {
            sb.append("disabled=true ");
        }

        if (options.getName() != null) {
            sb.append("name='").append(SafeHtmlUtils.htmlEscape(options.getName())).append("' ");
        }

        if (options.isReadonly() || !options.isEditable()) {
            sb.append("readonly ");
        }

        String placeholder = options.getEmptyText() != null ? " placeholder='" + SafeHtmlUtils.htmlEscape(options.getEmptyText()) + "' " : "";

        if ("".equals(value) && options.getEmptyText() != null) {
            sb.append(" ").append("field--empty");
            if (GXT.isIE8() || GXT.isIE9()) {
                value = options.getEmptyText();
            }
        }

        if (!options.isEditable()) {
            sb.append(" ").append("field--readonly");
        }

        sb.append("' ");
        sb.append(placeholder);

        sb.append("type='text' value='").append(SafeHtmlUtils.htmlEscape(value)).append("' ");

        sb.append("/>");

        shb.append(SafeHtmlUtils.fromTrustedString(sb.toString()));
    }
}
