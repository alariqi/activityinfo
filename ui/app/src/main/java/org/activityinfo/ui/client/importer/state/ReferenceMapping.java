package org.activityinfo.ui.client.importer.state;

import java.util.*;
import java.util.function.Predicate;

public class ReferenceMapping implements FieldMapping {
    private String fieldName;

    /**
     * Maps from columnId to the applicable key map
     */
    private Map<String, KeyMapping> columnMap;

    public ReferenceMapping(String fieldName, KeyMapping... mappings) {
        this(fieldName, Arrays.asList(mappings));
    }

    public ReferenceMapping(String fieldName, Iterable<KeyMapping> keyMappings) {
        this.fieldName = fieldName;
        this.columnMap = new HashMap<>();
        for (KeyMapping keyMapping : keyMappings) {
            columnMap.put(keyMapping.getColumnId(), keyMapping);
        }
    }

    private ReferenceMapping(String fieldName, Map<String, KeyMapping> columnMap) {
        this.fieldName = fieldName;
        this.columnMap = columnMap;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Set<String> getMappedColumnIds() {
        return columnMap.keySet();
    }

    @Override
    public Optional<FieldMapping> withColumns(Predicate<String> columnPredicate) {
        Map<String, KeyMapping> updatedMap = new HashMap<>();
        for (KeyMapping keyMapping : columnMap.values()) {
            if(columnPredicate.test(keyMapping.getColumnId())) {
                updatedMap.put(keyMapping.getColumnId(), keyMapping);
            }
        }
        if(updatedMap.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new ReferenceMapping(fieldName, updatedMap));
        }
    }

    public ReferenceMapping withKeyMapping(KeyMapping newMapping) {
        List<KeyMapping> updatedMappings = new ArrayList<>();
        for (KeyMapping existing : columnMap.values()) {
            if(!existing.conflictsWith(newMapping)) {
                updatedMappings.add(existing);
            }
        }
        updatedMappings.add(newMapping);

        return new ReferenceMapping(fieldName, updatedMappings);
    }

    @Override
    public String toString() {
        return "ReferenceMapping{" + fieldName + " => (" + columnMap.values() + ")}";
    }
}
