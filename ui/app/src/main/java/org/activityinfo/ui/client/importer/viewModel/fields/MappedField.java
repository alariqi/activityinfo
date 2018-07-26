package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;

import java.util.List;

public interface MappedField {

    List<MappedSourceColumn> getMappedColumns();

    boolean isComplete();

}
