package org.activityinfo.ui.client.importer.state;

import org.activityinfo.model.resource.ResourceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FieldMappingSet {
    private Map<String, FieldMapping> fieldMap;
    private Map<String, FieldMapping> columnMap;

    public FieldMappingSet() {
        fieldMap = new HashMap<>();
        columnMap = new HashMap<>();
    }

    public Optional<FieldMapping> getColumnMapping(String columnId) {
        return Optional.ofNullable(columnMap.get(columnId));
    }

    public boolean isSimpleMapped(ResourceId fieldId, String columnId) {
        return getColumnMapping(columnId).map(fieldMapping -> {
            if(fieldMapping instanceof SimpleMapping) {
                SimpleMapping simpleMapping = (SimpleMapping) fieldMapping;
                if(simpleMapping.getFieldId().equals(fieldId.asString()) &&
                        simpleMapping.getSourceColumnId().equals(columnId)) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public FieldMappingSet replaceSimpleMapping(String fieldName, String columnId) {

        SimpleMapping mapping = new SimpleMapping(fieldName, columnId);

        FieldMapping existingMapping = columnMap.get(columnId);
        if(mapping.equals(existingMapping)) {
            return this;
        }

        Map<String, FieldMapping> updatedFieldMap = new HashMap<>(fieldMap);
        Map<String, FieldMapping> updatedColumnMap = new HashMap<>(columnMap);


        throw new UnsupportedOperationException("TODO");
    }
}
