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
package org.activityinfo.ui.client.input.view.field;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.SerialNumber;
import org.activityinfo.model.type.SerialNumberType;

/**
 * FieldWidget for {@link org.activityinfo.model.type.SerialNumberType} fields.
 *
 * <p>Only displays values and does not allow input.</p>
 */
public class SerialNumberWidget implements FieldWidget {

    private TextField field;
    private SerialNumberType type;

    public SerialNumberWidget(SerialNumberType type) {
        this.type = type;
        this.field = new TextField();
        this.field.setReadOnly(true);
        this.field.setEmptyText(I18N.CONSTANTS.pending());
    }

    @Override
    public void init(FieldValue value) {
        field.setText(type.format(((SerialNumber) value)));
    }

    @Override
    public void clear() {
        field.clear();
    }

    @Override
    public void setRelevant(boolean relevant) {

    }

    @Override
    public void focus() {
    }

    @Override
    public Widget asWidget() {
        return field;
    }
}
