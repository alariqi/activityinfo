package org.activityinfo.ui.client.importer.viewModel.fields;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.LookupKey;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceViewModel;
import org.activityinfo.ui.client.input.viewModel.PermissionFilters;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrix;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrixSet;
import org.activityinfo.ui.client.store.FormStore;

import java.util.*;

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
    public java.util.Optional<MappedField> mapColumns(SourceViewModel source, FieldMappingSet fieldMappingSet) {

        Map<String, LookupKey> lookupKeyMap = new HashMap<>();

        for (LookupKey lookupKey : keySet.getLookupKeys()) {
            fieldMappingSet
                    .getMappedColumn(field.getName(), lookupKey.getKeyId())
                    .flatMap(columnId -> source.getColumnById(columnId))
                    .ifPresent(c -> {
                        lookupKeyMap.put(c.getId(), lookupKey);
                    });
        }

        if(lookupKeyMap.isEmpty()) {
            return java.util.Optional.empty();
        }

        LookupKey[] keys = new LookupKey[lookupKeyMap.size()];
        SourceColumn[] columns = new SourceColumn[lookupKeyMap.size()];

        int keyIndex = 0;
        for (SourceColumn sourceColumn : source.getColumns()) {
            if (lookupKeyMap.containsKey(sourceColumn.getId())) {
                keys[keyIndex] = lookupKeyMap.get(sourceColumn.getId());
                columns[keyIndex] = sourceColumn;
            }
        }

        KeyMatrix keyMatrix = chooseReferencedForm(keys);

        return java.util.Optional.of(new MappedReferenceField(field, keyMatrix, keys, columns));
    }

    /**
     * We *can* have fields that reference a record from one or more forms. For example, the "admin"
     * field of a location form can reference *either* a province, a district, or a territory.
     *
     * <p>Because these forms also reference each other, the set of keys overlap. We will choose to match against
     * the form based on which keys the user has chosen. For example, if they map only the province name, we will
     * match against the province form. If, however, province and district names are mapped, we will match against
     * the district.</p>
     *
     */
    private KeyMatrix chooseReferencedForm(LookupKey[] keys) {
        ReferenceType referenceType = (ReferenceType) field.getType();

        // If we have a single referenced form, we obviously match against that form
        // and do what we can with the keys we've been given.

        if(referenceType.getRange().size() == 1) {
            return keyMatrixSet.getMatrix(referenceType.getRange().iterator().next());
        }

        // The following is a heuristic...

        Set<ResourceId> forms = new HashSet<>();

        for (LookupKey key : keys) {
            if(referenceType.getRange().contains(key.getFormId())) {
                forms.add(key.getFormId());
            }
        }

        if(forms.size() == 1) {
            return keyMatrixSet.getMatrix(Iterables.getOnlyElement(forms));
        }

        throw new UnsupportedOperationException("TODO");
    }
}
