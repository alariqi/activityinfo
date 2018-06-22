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
package org.activityinfo.store.query.shared.join;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.store.query.shared.FilterLevel;
import org.activityinfo.store.spi.Slot;

import java.util.List;

public class JoinedColumnKey {

    private final FilterLevel filterLevel;
    private final List<ReferenceJoin> links;
    private final Slot<ColumnView> nestedColumn;

    public JoinedColumnKey(FilterLevel filterLevel, List<ReferenceJoin> links, Slot<ColumnView> nestedColumn) {
        this.filterLevel = filterLevel;
        this.links = links;
        this.nestedColumn = nestedColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinedColumnKey key = (JoinedColumnKey) o;

        if (filterLevel != key.filterLevel) return false;
        if (!links.equals(key.links)) return false;
        return nestedColumn.equals(key.nestedColumn);

    }

    @Override
    public int hashCode() {
        int result = filterLevel.hashCode();
        result = 31 * result + links.hashCode();
        result = 31 * result + nestedColumn.hashCode();
        return result;
    }
}
