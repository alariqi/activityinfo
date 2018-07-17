package org.activityinfo.ui.client.importer.viewModel.fields;

import com.google.common.base.Optional;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.LookupKey;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMapping;
import org.activityinfo.ui.client.importer.state.KeyMapping;
import org.activityinfo.ui.client.importer.state.ReferenceMapping;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceViewModel;
import org.activityinfo.ui.client.input.viewModel.PermissionFilters;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrixSet;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.List;

public class ReferenceViewModel extends FieldViewModel {

    private final List<ColumnTarget> targets;
    private final LookupKeySet keySet;
    private final KeyMatrixSet keyMatrixSet;

    public ReferenceViewModel(FormStore formStore, FormTree formTree, FormField field) {
        super(field);

        // Only include those that the user is authorized to choose
        PermissionFilters filters = new PermissionFilters(formTree);
        Optional<FormulaNode> filter = filters.getReferenceBaseFilter(field.getId());

        keySet = new LookupKeySet(formTree, field);
        keyMatrixSet = new KeyMatrixSet(formStore, (ReferenceType) field.getType(), keySet, filter);

        targets = new ArrayList<>();

        for (LookupKey lookupKey : keySet.getLookupKeys()) {
            targets.add(new ReferenceTarget(field, lookupKey, keyMatrixSet));
        }
    }

    @Override
    public List<ColumnTarget> getTargets() {
        return targets;
    }

    @Override
    public Observable<java.util.Optional<ImportedFieldViewModel>> computeImport(Observable<MappedSourceViewModel> source) {
        return Observable.just(java.util.Optional.empty());
    }

    @Override
    public java.util.Optional<String> columnMappingLabel(FieldMapping fieldMapping, String columnId) {
        if(fieldMapping instanceof ReferenceMapping) {
            ReferenceMapping referenceMapping = (ReferenceMapping) fieldMapping;
            for (KeyMapping keyMapping : referenceMapping.getKeyMappings()) {
                if(keyMapping.getColumnId().equals(columnId)) {
                    return findLookupKey(keyMapping).map(k -> k.getKeyLabel());
                }
            }
        }
        return java.util.Optional.empty();
    }

    private java.util.Optional<LookupKey> findLookupKey(KeyMapping mapping) {
        for (LookupKey lookupKey : keySet.getLookupKeys()) {
            if(lookupKey.getKeyId().equals(mapping.getKey())) {
                return java.util.Optional.of(lookupKey);
            }
        }
        return java.util.Optional.empty();
    }
}
