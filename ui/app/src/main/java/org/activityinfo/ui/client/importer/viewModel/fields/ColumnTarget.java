package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public interface ColumnTarget {

    /**
     * @return a human readable label for this target to be shown in the user interface.
     */
    String getLabel();


    boolean isApplied(String columnId, FieldMappingSet mappings);

    /**
     * Scores a source column on a scale from 0 to 1, based on how well the column's content
     * matches. For example, a quantity field will not score highly on a field with few numbers.
     *
     * This is meant to be a very rough measure that can be quickly computed.
     *
     */
    double scoreContent(SourceColumn column);

    /**
     * Applies the given {@code columnId} to this target, returning a new, updated
     * {@link FieldMappingSet}.
     */
    FieldMappingSet apply(FieldMappingSet mappingSet, String columnId);
}
