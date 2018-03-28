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
package org.activityinfo.ui.client.input;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;

public class RecordPlace extends Place {

    private ResourceId formId;
    private ResourceId recordId;

    public RecordPlace(ResourceId formId, ResourceId recordId) {
        this.formId = formId;
        this.recordId = recordId;
    }

    public ResourceId getFormId() {
        return formId;
    }

    public ResourceId getRecordId() {
        return recordId;
    }

    public RecordRef getRecordRef() {
        return new RecordRef(formId, recordId);
    }

    @Override
    public String toString() {
        return "record/" + formId.asString() + "/" + recordId.asString();
    }

    public static class Tokenizer implements PlaceTokenizer<RecordPlace> {
        @Override
        public RecordPlace getPlace(String token) {
            String[] parts = token.split("/");
            ResourceId formId = ResourceId.valueOf(parts[0]);
            ResourceId recordId = ResourceId.valueOf(parts[1]);

            return new RecordPlace(formId, recordId);
        }

        @Override
        public String getToken(RecordPlace place) {
            return place.getFormId() + "/" + place.getRecordId();
        }
    }
}
