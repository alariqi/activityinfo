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
package org.activityinfo.store.query.client.columns;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.store.query.client.join.SimpleForeignKeyBuilder;
import org.activityinfo.store.query.client.join.SimplePrimaryKeyMap;
import org.activityinfo.store.query.shared.columns.*;
import org.activityinfo.store.query.shared.join.PrimaryKeyMap;
import org.activityinfo.store.spi.CursorObserver;
import org.activityinfo.store.spi.ForeignKeyBuilder;
import org.activityinfo.store.spi.PendingSlot;

/**
 * Column Factory optimized for translation to JavaScript
 */
public class JsColumnFactory implements ColumnFactory {

    public static final JsColumnFactory INSTANCE = new JsColumnFactory();

    @Override
    public CursorObserver<FieldValue> newStringBuilder(PendingSlot<ColumnView> result, StringReader reader) {
        return new StringColumnBuilder(result, reader);
    }

    @Override
    public CursorObserver<FieldValue> newDoubleBuilder(PendingSlot<ColumnView> result, DoubleReader reader) {
        return new SimpleDoubleColumnBuilder(result, reader);
    }

    @Override
    public CursorObserver<FieldValue> newEnumBuilder(PendingSlot<ColumnView> result, EnumType enumType) {
        return new SimpleEnumColumnBuilder(result, enumType);
    }

    @Override
    public ForeignKeyBuilder newForeignKeyBuilder(ResourceId rightFormId, PendingSlot<ForeignKey> value) {
        return new SimpleForeignKeyBuilder(rightFormId, value);
    }

    @Override
    public PrimaryKeyMap newPrimaryKeyMap(ColumnView columnView) {
        return new SimplePrimaryKeyMap(columnView);
    }
}
