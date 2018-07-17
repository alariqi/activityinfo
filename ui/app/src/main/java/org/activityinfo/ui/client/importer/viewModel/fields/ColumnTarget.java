package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public interface ColumnTarget {

    String getLabel();

    boolean isSelected(String columnId, FieldMappingSet mappings);

    /**
     * Scores a source column on a scale from 0 to 1, based on how well the column's content
     * matches. For example, a quantity field will not score highly on a field with few numbers.
     *
     * This is meant to be a very rough measure that can be quickly computed.
     *
     */
    double scoreContent(SourceColumn column);

    FieldMappingSet apply(FieldMappingSet mappingSet, String columnId);
}
