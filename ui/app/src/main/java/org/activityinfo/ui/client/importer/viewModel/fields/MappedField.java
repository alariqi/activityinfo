package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;

import java.util.List;

public interface MappedField {

    FormField getField();

    List<MappedSourceColumn> getMappedColumns();

    /**
     * @return true if this mapping is complete. For example, both latitude and longitude are mapped.
     */
    boolean isComplete();

    /**
     * Creates an FieldImporter implementation that can be used to parse this field's values.
     * @throws IllegalStateException if {@link #isComplete()}) is {@code false}
     */
    Observable<FieldImporter> getImporter();
}
