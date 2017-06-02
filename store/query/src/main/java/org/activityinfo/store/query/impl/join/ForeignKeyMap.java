package org.activityinfo.store.query.impl.join;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.activityinfo.model.resource.ResourceId;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * Maps row indices to reference values for a given field.
 */
public class ForeignKeyMap implements Serializable {

    public static final ForeignKeyMap EMPTY =
            new ForeignKeyMap(0, Multimaps.forMap(Collections.<Integer, ResourceId>emptyMap()));

    /**
     * Maps row index to ResourceId of related entity
     */
    private Multimap<Integer, ResourceId> keys;
    private int numRows;

    public ForeignKeyMap(int numRows, Multimap<Integer, ResourceId> keys) {
        this.numRows = numRows;
        this.keys = keys;
    }

    public int getNumRows() {
        return numRows;
    }

    public Collection<ResourceId> getKeys(int rowIndex) {
        return keys.get(rowIndex);
    }
}
