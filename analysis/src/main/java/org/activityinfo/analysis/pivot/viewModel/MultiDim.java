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
package org.activityinfo.analysis.pivot.viewModel;

import java.util.BitSet;

class MultiDim {
    private int index;
    private String[] labels;
    private final BitSet[] bitSets;

    MultiDim(int dimensionIndex, String[] labels, BitSet[] bitSets) {
        this.index = dimensionIndex;
        this.labels = labels;
        this.bitSets = bitSets;
    }

    int getCategoryCount() {
        return labels.length;
    }

    public BitSet getBitSet(int categoryIndex) {
        return bitSets[categoryIndex];
    }

    public int getDimensionIndex() {
        return index;
    }

    public String getLabel(int categoryIndex) {
        return labels[categoryIndex];
    }

    public int countExpandedRows() {
        int count = 0;
        for (BitSet bitSet : bitSets) {
            count += bitSet.cardinality();
        }
        return count;
    }
}
