package org.activityinfo.ui.client.importer.state;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface FieldMapping {

    String getFieldName();

    Set<String> getMappedColumnIds();

    /**
     * Returns a new field mapping only with the field mappings that meet the given
     * {@code columnPredicate}, or {@code Optional.empty()} if there are no mappings left.
     */
    Optional<FieldMapping> withColumns(Predicate<String> columnPredicate);


}
