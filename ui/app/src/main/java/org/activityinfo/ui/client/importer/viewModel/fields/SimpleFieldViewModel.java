package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.parser.FieldParser;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SimpleFieldViewModel extends FieldViewModel {

    private final FormField field;
    private final FieldParser parser;

    public SimpleFieldViewModel(FormField field, FieldParser parser) {
        super(field);
        this.field = field;
        this.parser = parser;
    }

    @Override
    public Collection<ColumnTarget> unusedTarget(FieldMappingSet explicitMappings) {
        if(explicitMappings.isFieldMapped(field.getName())) {
            return Collections.singletonList(new SimpleColumnTarget(field, parser));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ColumnTarget> getTargets() {
        return Collections.singletonList(new SimpleColumnTarget(field, parser));
    }
}
