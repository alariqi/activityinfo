package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;

import java.util.Collection;
import java.util.List;

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

    public abstract Collection<ColumnTarget> unusedTarget(FieldMappingSet explicitMappings);

    public abstract List<ColumnTarget> getTargets();
}
