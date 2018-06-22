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
package org.activityinfo.store.query.server.columns;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.enumerated.EnumValue;
import org.activityinfo.store.query.shared.columns.DiscreteStringColumnView;
import org.activityinfo.store.spi.CursorObserver;
import org.activityinfo.store.spi.PendingSlot;

import java.util.Map;

/**
 * Constructs ColumnViews for fields with enumerated type that chooses
 * a storage form to minimize the amount of memory required to store the column.
 *
 */
public class CompactingEnumColumnBuilder implements CursorObserver<FieldValue> {

    private final PendingSlot<ColumnView> result;

    private final Map<ResourceId, Integer> labelIndexMap = Maps.newHashMap();
    private final String[] ids;
    private final String[] labels;
    private IntArrayList values = new IntArrayList();

    public CompactingEnumColumnBuilder(PendingSlot<ColumnView> result, EnumType enumType) {
        this.result = result;

        int index = 0;
        this.ids = new String[enumType.getValues().size()];
        this.labels = new String[enumType.getValues().size()];
        for (EnumItem item : enumType.getValues()) {
            this.ids[index] = item.getId().asString();
            this.labels[index] = item.getLabel();
            this.labelIndexMap.put(item.getId(), index);
            index++;
        }
    }

    private byte[] createIndexArray8() {
        byte indexes[] = new byte[values.size()];
        for (int i = 0; i != indexes.length; ++i) {
            indexes[i] = (byte)values.getInt(i);
        }
        return indexes;
    }

    @Override
    public void onNext(FieldValue value) {
        values.add(indexOf(value));
    }

    private int indexOf(FieldValue value) {
        if (value instanceof EnumValue) {
            EnumValue fieldValue = (EnumValue) value;
            if (fieldValue.getResourceIds().size() == 1) {
                Integer index = labelIndexMap.get(fieldValue.getValueId());
                if (index != null) {
                    return index;
                }
            }
        }
        return -1;
    }

    @Override
    public void done() {
        result.set(build());
    }

    @VisibleForTesting
    ColumnView build() {
        if(labels.length < DiscreteStringColumnView8.MAX_COUNT) {
            return build8();
        } else {
            return build32();
        }
    }

    @VisibleForTesting
    ColumnView build8() {
        return new DiscreteStringColumnView8(ids, labels, createIndexArray8());
    }

    @VisibleForTesting
    ColumnView build32() {
        return new DiscreteStringColumnView(ids, labels, values.toIntArray());
    }

}
