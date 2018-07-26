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
package org.activityinfo.model.query;

import org.activityinfo.json.Json;
import org.activityinfo.json.JsonSerializable;
import org.activityinfo.json.JsonValue;

import java.util.List;

public class SortModel implements JsonSerializable {

    private String field;
    private SortDir dir = SortDir.ASC;

    /**
     * The {@code Range} stores the indices of a group's member elements.
     * Range object allows for efficient re-selection of various row ranges,
     * limiting the creation of new int array instances
     */
    public static class Range {

        // max array length set during initial construction
        private int[] rows;
        private int endIndex;

        public Range(int from, int to) {
            this.rows = new int[to-from+1];
            for (int i=0; i<rows.length; i++) {
                rows[i] = from + i;
            }
            this.endIndex = rows.length-1;
        }

        public Range(int[] range) {
            this.rows = range;
            this.endIndex = rows.length-1;
        }

        public Range(List<Integer> range) {
            this.rows = new int[range.size()];
            for (int i=0; i<range.size(); i++) {
                rows[i] = range.get(i);
            }
            this.endIndex = rows.length-1;
        }

        public int getRangeSize() {
            return endIndex+1;
        }

        /**
         * Returns the currently selected row range.
         * Only creates a new int array when a subset of the full range is selected.
         */
        public int[] getRange() {
            return endIndex == (rows.length-1) ? rows : copySubset(rows,0, endIndex);
        }

        public Range resetRange() {
            endIndex = -1;
            return this;
        }

        public void addToRange(int row) {
            rows[endIndex+1] = row;
            endIndex++;
        }

        public int getStart() {
            return endIndex == -1 ? -1 : rows[0];
        }

        public int getEnd() {
            return endIndex == -1 ? -1 : rows[endIndex];
        }

        private int[] copySubset(int[] array, int from, int to) {
            int[] subset = new int[to-from+1];
            for (int i=0; i<subset.length; i++) {
                subset[i] = array[from + i];
            }
            return subset;
        }
    }

    public SortModel() {}

    public SortModel(String field, SortDir dir) {
        this.field = field;
        this.dir = dir;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setDir(SortDir dir) {
        this.dir = dir;
    }

    public String getField() {
        return field;
    }

    public SortDir getDir() {
        return dir;
    }


    @Override
    public JsonValue toJson() {
        JsonValue object = Json.createObject();
        object.put("field", getField());
        object.put("dir", getDir().name());
        return object;
    }

    public static SortModel fromJson(JsonValue object) {
        SortModel sortModel = new SortModel();
        if (object.hasKey("field")) {
            sortModel.setField(object.getString("field"));
        }
        if (object.hasKey("dir")) {
            sortModel.setDir(SortDir.valueOf(object.getString("dir")));
        }
        return sortModel;
    }

}
