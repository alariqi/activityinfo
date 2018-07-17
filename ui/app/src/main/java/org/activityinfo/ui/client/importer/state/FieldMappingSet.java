package org.activityinfo.ui.client.importer.state;

import org.activityinfo.io.match.coord.CoordinateAxis;

import java.util.*;

/**
 * A set of mappings from form fields to imported columns.
 *
 * <p>This class is immutable, and maintains the following invariants:
 * <ul>
 *     <li>Each field can have only one mapping</li>
 *     <li>Each column can be used in one field mapping</li>
 * </ul>
 */
public class FieldMappingSet {
    private Map<String, FieldMapping> fieldMap;
    private Map<String, FieldMapping> columnMap;
    private Set<String> ignoredColumns;

    public FieldMappingSet() {
        fieldMap = new HashMap<>();
        columnMap = new HashMap<>();
        ignoredColumns = new HashSet<>();
    }

    private FieldMappingSet(Iterable<FieldMapping> mappings, Set<String> ignoredColumns) {
        this.ignoredColumns = ignoredColumns;
        this.fieldMap = new HashMap<>();
        this.columnMap = new HashMap<>();
        for (FieldMapping fieldMapping : mappings) {
            fieldMap.put(fieldMapping.getFieldName(), fieldMapping);
            for (String columnId : fieldMapping.getMappedColumnIds()) {
                columnMap.put(columnId, fieldMapping);
            }
        }
    }

    /**
     *
     * @return the set of column ids that are either part of a field mapping, or are explicitly ignored.
     */
    public Set<String> getMappedColumnIds() {
        return columnMap.keySet();
    }

    public Optional<FieldMapping> getColumnMapping(String columnId) {
        return Optional.ofNullable(columnMap.get(columnId));
    }

    private FieldMappingSet withMapping(FieldMapping newMapping) {

        // We have to ensure that each column is mapped to no more than one
        // field mapping. If i
        Set<String> newlyMappedColumns = newMapping.getMappedColumnIds();

        // Copy any existing mappings, removing this co
        List<FieldMapping> retained = new ArrayList<>();
        for (FieldMapping existing : getMappings()) {
            if(!existing.getFieldName().equals(newMapping.getFieldName())) {
                Optional<FieldMapping> updated = existing.withColumns(c -> !newlyMappedColumns.contains(c));
                updated.ifPresent(m -> retained.add(m));
            }
        }
        // Add the new field mapping
        retained.add(newMapping);

        // Remove any mapped columns from the ignored set
        Set<String> updatedIgnore = new HashSet<>(ignoredColumns);
        updatedIgnore.removeAll(newlyMappedColumns);

        return new FieldMappingSet(retained, updatedIgnore);

    }

    public FieldMappingSet withSimpleMapping(String fieldName, String columnId) {
        return withMapping(new SimpleFieldMapping(fieldName, columnId));
    }

    public FieldMappingSet withReferenceMapping(String fieldName, KeyMapping keyMapping) {
        FieldMapping existing = fieldMap.get(fieldName);
        if(existing instanceof ReferenceMapping) {
            return withMapping(((ReferenceMapping) existing).withKeyMapping(keyMapping));
        } else {
            return withMapping(new ReferenceMapping(fieldName, keyMapping));
        }
    }

    public FieldMappingSet withGeoPointMapping(String fieldName, CoordinateAxis axis, String columnId) {

        FieldMapping existing = fieldMap.get(fieldName);
        if(existing instanceof GeoPointMapping) {
            return withMapping(((GeoPointMapping) existing).withCoordMapping(axis, columnId));
        } else {
            return withMapping(new GeoPointMapping(fieldName, axis, columnId));
        }
    }

    public Optional<FieldMapping> getFieldMapping(String fieldName) {
        return Optional.ofNullable(fieldMap.get(fieldName));
    }

    public Collection<FieldMapping> getMappings() {
        return fieldMap.values();
    }

    public boolean isFieldMapped(String name) {
        return fieldMap.containsKey(name);
    }
}
