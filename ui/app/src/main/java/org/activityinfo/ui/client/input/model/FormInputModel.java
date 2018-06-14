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
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.RecordRef;

import java.util.*;

/**
 * The state of the user's input to add or change a record.
 */
public class FormInputModel {

    private final RecordRef recordRef;
    private final Map<ResourceId, FieldInput> fieldInputs;
    private final Set<ResourceId> touchedFields;
    private final boolean validationRequested;

    public FormInputModel(RecordRef recordRef) {
        this.recordRef = recordRef;
        fieldInputs = Collections.emptyMap();
        touchedFields = Collections.emptySet();
        validationRequested = false;
    }

    private FormInputModel(RecordRef recordRef,
                           Map<ResourceId, FieldInput> fieldInputs,
                           Set<ResourceId> touchedFields,
                           boolean validationRequested) {
        this.recordRef = recordRef;
        this.fieldInputs = fieldInputs;
        this.touchedFields = touchedFields;
        this.validationRequested = validationRequested;
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

        Set<ResourceId> updatedTouchSet = add(this.touchedFields, fieldId);

        return new FormInputModel(this.recordRef, updatedInputs, updatedTouchSet, this.validationRequested);
    }

    public FormInputModel update(ResourceId fieldId, FieldValue value) {
        return update(fieldId, new FieldInput(value));
    }

    public FormInputModel touch(ResourceId fieldId) {
        if(this.touchedFields.contains(fieldId)) {
            return this;
        } else {
            return new FormInputModel(recordRef, fieldInputs, add(touchedFields, fieldId), validationRequested);
        }
    }

    public FormInputModel validationRequested() {
        return new FormInputModel(recordRef, fieldInputs, touchedFields, true);
    }

    /**
     * @return true if the user has explicitly requested validation for this form,
     * by example clicking the save button.
     */
    public boolean isValidationRequested() {
        return validationRequested;
    }

    /**
     * @return true if the user has "touched" the given field in any way, and so should,
     * for example, see a validation message of this field.
     */
    public boolean isTouched(ResourceId fieldId) {
        return touchedFields.contains(fieldId);
    }

    private static Set<ResourceId> add(Set<ResourceId> set, ResourceId fieldId) {
        if(set.contains(fieldId)) {
            return set;
        } else {
            Set<ResourceId> newSet = new HashSet<>(set);
            newSet.add(fieldId);
            return newSet;
        }
    }
}
