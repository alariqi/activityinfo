package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMapping;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceViewModel;

import java.util.List;
import java.util.Optional;

/**
 * Centralizes our knowledge about an existing field to which imported columns can be matched.
 *
 * <p>Depending on its type, a field can be "bound" to one or more "targets", which are represented by the
 * {@link ColumnTarget} interface.</p>
 *
 * <p>Most field types are considered "simple" and can be bound only to a single column. For example,
 * fields of TextType, LocalDateType, are all considered to be simple and can be matched to a single target.</p>
 *
 * <p>GeoPoint fields must be bound to exactly two fields in odrer </p>
 */
public abstract class FieldViewModel {

    protected final FormField field;

    public FieldViewModel(FormField field) {
        this.field = field;
    }

    public FormField getField() {
        return field;
    }

    public abstract List<ColumnTarget> getTargets();

    public String getFieldName() {
        return field.getName();
    }

    public abstract Observable<Optional<ImportedFieldViewModel>> computeImport(Observable<MappedSourceViewModel> source);

    public abstract Optional<String> columnMappingLabel(FieldMapping fieldMapping, String columnId);
}
