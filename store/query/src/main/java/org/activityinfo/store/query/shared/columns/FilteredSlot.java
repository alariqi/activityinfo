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
package org.activityinfo.store.query.shared.columns;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.store.query.shared.Slot;
import org.activityinfo.store.query.shared.TableFilter;


public class FilteredSlot implements Slot<ColumnView> {
    private Slot<TableFilter> filterSlot;
    private Slot<ColumnView> columnViewSlot;

    private ColumnView filtered = null;

    public FilteredSlot(Slot<TableFilter> filterSlot, Slot<ColumnView> columnViewSlot) {
        this.filterSlot = filterSlot;
        this.columnViewSlot = columnViewSlot;
    }


    @Override
    public ColumnView get() {
        if(filtered == null) {
            filtered = filterSlot.get().apply(columnViewSlot.get());
        }
        return filtered;
    }
}
