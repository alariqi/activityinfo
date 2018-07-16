package org.activityinfo.ui.client.importer.state;

import java.util.Collections;
import java.util.Set;

/**
 * A simple one-to-one mapping between a single column to import and a column to
 */
public class SimpleMapping implements FieldMapping {
    private String fieldId;
    private String sourceColumnId;

    public SimpleMapping(String fieldId, String sourceColumnId) {
        this.sourceColumnId = sourceColumnId;
        this.fieldId = fieldId;
    }

    public String getSourceColumnId() {
        return sourceColumnId;
    }

    @Override
    public String getFieldId() {
        return fieldId;
    }

    @Override
    public Set<String> getMappedColumnIds() {
        return Collections.singleton(sourceColumnId);
    }
}
