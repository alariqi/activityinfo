package org.activityinfo.ui.client.importer.state;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A simple one-to-one mapping between a single column to import and a single field.
 */
public class SimpleFieldMapping implements FieldMapping {
    private String fieldId;
    private String sourceColumnId;

    public SimpleFieldMapping(String fieldId, String sourceColumnId) {
        this.sourceColumnId = sourceColumnId;
        this.fieldId = fieldId;
    }

    public String getSourceColumnId() {
        return sourceColumnId;
    }

    @Override
    public String getFieldName() {
        return fieldId;
    }

    @Override
    public Set<String> getMappedColumnIds() {
        return Collections.singleton(sourceColumnId);
    }

    @Override
    public Optional<FieldMapping> withColumns(Predicate<String> columnPredicate) {
        if(columnPredicate.test(sourceColumnId)) {
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "SimpleFieldMapping{" +
                fieldId + " => " +
                sourceColumnId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleFieldMapping that = (SimpleFieldMapping) o;
        return Objects.equals(fieldId, that.fieldId) &&
                Objects.equals(sourceColumnId, that.sourceColumnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId, sourceColumnId);
    }
}
