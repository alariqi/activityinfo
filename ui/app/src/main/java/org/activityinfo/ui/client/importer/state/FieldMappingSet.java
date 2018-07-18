package org.activityinfo.ui.client.importer.state;

import com.google.common.collect.Lists;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * A set of mappings from form fields to imported columns.
 *
 * <p>Each mapping between a field and a column has a <em>role</em>. For simple mappings between
 * a single column and a simple field of {@code TextType} or {@code LocalDateType}, there will
 * be a single mapping with the role "VALUE".
 *
 * <p>For fields of {@code GeoPointType}, there will up to two mappings, one with the role "LATITUDE" and
 * the other with the role "LONGITUDE"</p>
 *
 * <p>This class is immutable, and maintains the following invariants:
 * <ul>
 *     <li>Each field-role pair can have only one mapping</li>
 *     <li>Each column can be used in one field mapping</li>
 * </ul>
 */
public class FieldMappingSet implements Iterable<FieldMapping> {


    /**
     * List of field-role-column mappings.
     */
    private List<FieldMapping> list;

    /**
     * Maps from column id to FieldMapping
     */
    private Map<String, FieldMapping> columnMap;

    /**
     * Maps from "fieldName$$role" to column id
     */
    private Map<String, String> index;

    /**
     * A set of column ids that have been explicitly marked as "ignored" by the user.
     */
    private Set<String> ignoredColumns;

    public FieldMappingSet() {
        this(Collections.emptyList(), Collections.emptySet());
    }

    private FieldMappingSet(Iterable<FieldMapping> list, Set<String> ignoredColumns) {
        this.list = Lists.newArrayList(list);
        this.ignoredColumns = ignoredColumns;
        this.columnMap = new HashMap<>();
        this.index = new HashMap<>();
        for (FieldMapping fieldMapping : list) {
            columnMap.put(fieldMapping.getColumnId(), fieldMapping);
            index.put(indexKey(fieldMapping.getFieldName(), fieldMapping.getRole()), fieldMapping.getColumnId());
        }
    }

    private static String indexKey(String fieldName, String role) {
        return fieldName + "$$" + role;
    }

    /**
     * @return the columnId of the column mapped to the given {@code fieldName} and {@code role}, if one exists.
     */
    public Optional<String> getMappedColumn(String fieldName, String role) {
        return Optional.ofNullable(index.get(indexKey(fieldName, role)));
    }

    /**
     * @return the columnId of the column mapped to the given {@code fieldName} in the VALUE role, if one exists.
     */
    public Optional<String> getMappedValueColumn(String fieldName) {
        return getMappedColumn(fieldName, FieldMapping.VALUE_ROLE);
    }

    public FieldMappingSet withMapping(FieldMapping newMapping) {

        // Create a new list
        List<FieldMapping> newList = new ArrayList<>();
        newList.add(newMapping);

        // Keep any existing mappings that don't conflict with this mapping
        for (FieldMapping mapping : this.list) {
            if(!mapping.conflicts(newMapping)) {
                newList.add(mapping);
            }
        }
        // Remove any mapped columns from the ignored set
        Set<String> updatedIgnore = new HashSet<>(ignoredColumns);
        updatedIgnore.remove(newMapping.getColumnId());

        return new FieldMappingSet(newList, updatedIgnore);
    }

    public FieldMappingSet withMapping(String fieldName, String role, String columnId) {
        return withMapping(new FieldMapping(fieldName, role, columnId));
    }

    public FieldMappingSet withColumnIgnored(String columnId) {

        // Copy any existing mappings, removing this column from any existing mapping
        List<FieldMapping> newList = list.stream().filter(m -> !m.getColumnId().equals(columnId)).collect(toList());

        Set<String> newIgnored = new HashSet<>(ignoredColumns);
        newIgnored.add(columnId);


        return new FieldMappingSet(newList, newIgnored);
    }


    /**
     * Returns {@code true} if the given {@code columnId} is mapped to the given {@code fieldName} and {@code role}
     */
    public boolean isMapped(String fieldName, String role, String columnId) {
        return columnId.equals(index.get(indexKey(fieldName, role)));
    }

    public boolean isIgnored(String id) {
        return ignoredColumns.contains(id);
    }

    @Override
    public Iterator<FieldMapping> iterator() {
        return list.iterator();
    }

}
