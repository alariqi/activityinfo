package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.ReferenceType;

import java.util.*;

public class FieldListBuilder {

    private final Set<ResourceId> visitedForms = new HashSet<>();
    private final List<FieldListItem> fields = new ArrayList<>();
    private Set<String> includedFormulas;
    private Map<ResourceId, FormClass> formMap = new HashMap<>();

    public FieldListBuilder(Set<String> includedFormulas) {
        this.includedFormulas = includedFormulas;
    }

    /**
     * Create a list from a single root form
     */
    public FieldListViewModel build(FormTree formTree) {

        indexFormTree(formTree);
        addRootFields(formTree.getRootFormClass());
        visitedForms.add(formTree.getRootFormId());

        findReferenceForms(formTree.getRootFormClass());

        return new FieldListViewModel(fields);
    }

    private void indexFormTree(FormTree formTree) {
        for (FormMetadata form : formTree.getForms()) {
            if(form.isVisible()) {
                formMap.put(form.getId(), form.getSchema());
            }
        }
    }


    private void addRootFields(FormClass formClass) {
        for (FormField field : formClass.getFields()) {
            if(!(field.getType() instanceof ReferenceType)) {
                fields.add(FieldListItem.builder()
                        .rootField(field)
                        .included(includedFormulas.contains(field.getName()))
                        .build());
            }
        }
    }

    private void findReferenceForms(FormClass parentForm) {
        for (FormField field : parentForm.getFields()) {
            if(field.getType() instanceof ReferenceType) {
                ReferenceType referenceType = (ReferenceType) field.getType();
                for (ResourceId formId : referenceType.getRange()) {
                    boolean unvisited = visitedForms.add(formId);
                    if(unvisited) {
                        FormClass referencedForm = formMap.get(formId);
                        if(referencedForm != null) {
                            addReferenceFields(referencedForm);
                        }
                    }
                }
            }
        }
    }

    private void addReferenceFields(FormClass formClass) {
        for (FormField field : formClass.getFields()) {
            if(!(field.getType() instanceof ReferenceType)) {
                String formula = formClass.getId() + "." + field.getName();
                fields.add(FieldListItem.builder()
                        .form(formClass.getLabel())
                        .field(field)
                        .included(includedFormulas.contains(formula))
                        .build());
            }
        }
    }
}
