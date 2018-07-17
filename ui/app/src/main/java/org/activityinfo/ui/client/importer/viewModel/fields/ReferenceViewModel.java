package org.activityinfo.ui.client.importer.viewModel.fields;

import com.google.common.base.Optional;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.LookupKey;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.input.viewModel.PermissionFilters;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrixSet;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReferenceViewModel extends FieldViewModel {

    private final List<ColumnTarget> targets;

    public ReferenceViewModel(FormStore formStore, FormTree formTree, FormField field) {
        super(field);

        // Only include those that the user is authorized to choose
        PermissionFilters filters = new PermissionFilters(formTree);
        Optional<FormulaNode> filter = filters.getReferenceBaseFilter(field.getId());

        LookupKeySet keySet = new LookupKeySet(formTree, field);
        KeyMatrixSet keyMatrixSet = new KeyMatrixSet(formStore, (ReferenceType) field.getType(), keySet, filter);

        targets = new ArrayList<>();

        for (LookupKey lookupKey : keySet.getLookupKeys()) {
            targets.add(new ReferenceTarget(field, lookupKey, keyMatrixSet));
        }
    }

    @Override
    public Collection<ColumnTarget> unusedTarget(FieldMappingSet explicitMappings) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> getTargets() {
        return targets;
    }
}
