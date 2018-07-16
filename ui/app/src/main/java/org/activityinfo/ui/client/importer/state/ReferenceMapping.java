package org.activityinfo.ui.client.importer.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReferenceMapping implements FieldMapping {
    private String fieldId;

    /**
     * Mapping from column ids to reference field keys
     */
    private Map<String, String> keyMapping = new HashMap<>();

    public ReferenceMapping(String fieldId, Map<String, String> keyMapping) {
        this.fieldId = fieldId;
        this.keyMapping = keyMapping;
    }

    @Override
    public String getFieldId() {
        return fieldId;
    }

    @Override
    public Set<String> getMappedColumnIds() {
        return keyMapping.keySet();
    }
}
