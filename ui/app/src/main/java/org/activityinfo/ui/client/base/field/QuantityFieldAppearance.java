/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.base.field;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.core.client.dom.XElement;

public class QuantityFieldAppearance extends Css3TwinTriggerFieldAppearance {

    private String units;

    public QuantityFieldAppearance(String units) {
        super();
        this.units = units;
    }

    @Override
    public void onResize(XElement parent, int width, int height, boolean hideTrigger) {
    }

    @Override
    public void onTriggerClick(XElement parent, boolean click) {
        // NOOP: No trigger
    }

    @Override
    public void onTriggerOver(XElement parent, boolean over) {
        // NOOP: No trigger
    }

    @Override
    protected void renderTrigger(SafeHtmlBuilder sb, String value, FieldCell.FieldAppearanceOptions options) {
        sb.appendHtmlConstant("<div class=\"field__units\"><span>");
        sb.appendEscaped(units);
        sb.appendHtmlConstant("</span>");
    }

    @Override
    public boolean triggerIsOrHasChild(XElement parent, Element target) {
        return false;
    }


}
