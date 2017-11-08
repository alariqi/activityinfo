package org.activityinfo.model.formTree;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.form.FormPermissions;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordFieldType;

import java.util.List;
import java.util.logging.Logger;

/**
 * Constructs a tree of related Forms from a given root FormClass.
 */
public class FormTreeBuilder {

    private static final Logger LOGGER = Logger.getLogger(FormTreeBuilder.class.getName());

    private final FormMetadataProvider metadataProvider;

    public FormTreeBuilder(final FormClassProvider schemaProvider) {
        metadataProvider = new FormMetadataProvider() {
            @Override
            public FormMetadata getFormMetadata(ResourceId formId) {
                FormClass formClass = schemaProvider.getFormClass(formId);
                return FormMetadata.of(1L, formClass, FormPermissions.readonly());
            }
        };
    }

    public FormTreeBuilder(FormMetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
    }

    public FormTree queryTree(ResourceId rootFormId) {

        FormTree tree = new FormTree(rootFormId);
        FormMetadata root = metadataProvider.getFormMetadata(rootFormId);

        if(root.isDeleted()) {
            tree.setRootState(FormTree.State.DELETED);
            return tree;
        }

        if(!root.isVisible()) {
            tree.setRootState(FormTree.State.FORBIDDEN);
            return tree;
        }

        FormClass rootSchema = root.getSchema();

        List<ResourceId> stack = Lists.newArrayList(rootFormId);

        Optional<FormField> parentField = rootSchema.getParentField();
        if(parentField.isPresent()) {
            if(!stack.contains(parentField.get().getId())) {
                FormTree.Node node = tree.addRootField(root, parentField.get());
                fetchChildren(stack, node, rootSchema.getParentFormId().asSet());
            }
        }

        // Add fields defined by this FormClass
        for(FormField field : rootSchema.getFields()) {
            FormTree.Node node = tree.addRootField(root, field);
            if(node.isReference()) {
                fetchChildren(stack, node, node.getRange());
            } else if(field.getType() instanceof RecordFieldType) {
                addChildren(stack, node, embeddedForm(node));
            }
        }
        return tree;
    }

    /**
     * Now that we have the actual FormClass model that corresponds to this node's
     * formClassId, add it's children.
     *
     */
    private void fetchChildren(List<ResourceId> stack, FormTree.Node parent, Iterable<ResourceId> childFormIds)  {
        for(ResourceId childFormId : childFormIds) {
            if(!stack.contains(childFormId)) {
                FormMetadata childMetadata =  metadataProvider.getFormMetadata(childFormId);
                FormClass childSchema = childMetadata.getSchema();
                assert childSchema != null;
                assert childSchema.getId().equals(childFormId);
                addChildren(stack, parent, childMetadata);
            }
        }
    }

    private void addChildren(List<ResourceId> stack, FormTree.Node parent, FormMetadata childForm) {
        stack.add(parent.getDefiningFormClass().getId());
        try {
            for (FormField field : childForm.getFields()) {
                FormTree.Node childNode = parent.addChild(childForm, field);
                if (childNode.isReference()) {
                    fetchChildren(stack, childNode, childNode.getRange());
                } else if (childNode.getType() instanceof RecordFieldType) {
                    addChildren(stack, childNode, embeddedForm(childNode));
                }
            }
        } finally {
            stack.remove(stack.size() - 1);
        }
    }

    private FormMetadata embeddedForm(FormTree.Node childNode) {
        FormClass embeddedFormSchema = ((RecordFieldType) childNode.getType()).getFormClass();
        long formVersion = 1L;

        return FormMetadata.of(formVersion, embeddedFormSchema, FormPermissions.readonly());
    }

}
