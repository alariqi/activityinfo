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
package org.activityinfo.ui.client.input.model;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The state of the user's input into the form as well any sub forms.
 * Immutable.
 */
public class FormInputModel {

    private final RecordRef recordRef;
    private final Map<ResourceId, FieldInput> fieldInputs;

    public FormInputModel(RecordRef recordRef) {
        this.recordRef = recordRef;
        fieldInputs = Collections.emptyMap();
    }

    private FormInputModel(RecordRef recordRef,
                           Map<ResourceId, FieldInput> fieldInputs) {
        this.recordRef = recordRef;
        this.fieldInputs = fieldInputs;
    }

    public FieldInput get(ResourceId fieldId) {
        FieldInput fieldInput = fieldInputs.get(fieldId);
        if(fieldInput == null) {
            return FieldInput.UNTOUCHED;
        }
        return fieldInput;
    }

    public RecordRef getRecordRef() {
        return recordRef;
    }


    /**
     * Returns a new, updated version of this model with the change to the given field on the given
     * record.
     *
     * @param fieldId the id of the field to change
     * @param input the user's input.
     * @return a new copy of
     */
    public FormInputModel update(ResourceId fieldId, FieldInput input) {

        Map<ResourceId, FieldInput> updatedInputs = new HashMap<>(this.fieldInputs);
        updatedInputs.put(fieldId, input);

        return new FormInputModel(this.recordRef, updatedInputs);
    }

}
