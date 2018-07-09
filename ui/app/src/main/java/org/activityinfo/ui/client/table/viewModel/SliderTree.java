package org.activityinfo.ui.client.table.viewModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.subform.SubFormReferenceType;

import java.util.*;

public class SliderTree {

    private final ResourceId rootFormId;

    /**
     * Maps each formId in this tree to its depth in the hierarchy, where a depth of zero is the
     * root form, a depth of one is a sub-form of the root form, and so on.
     */
    private final Map<ResourceId, Integer> depthMap;
    private final Map<ResourceId, ResourceId> parentMap;
    private final List<ResourceId> formIds;
    private final int slideCount;

    public SliderTree(FormTree formTree) {

        // Find the root form of the slider tree -- all other forms are subforms directly
        // or indirectly.
        rootFormId = findRootForm(formTree);

        // Find the parents of all subforms
        parentMap = new HashMap<>();
        for (FormMetadata form : formTree.getForms()) {
            if(form.isVisible() && form.getSchema().isSubForm()) {
                parentMap.put(form.getId(), form.getSchema().getParentFormId().get());
            }
        }

        // Find all the sub(forms) in this hierarchy and their depth relative to the root form
        depthMap = collectSubForms(formTree, rootFormId);

        // The total number of "slides" is equal to the maximum depth of the hierarchy
        // and our current slide index is equal to the active form's position in the path
        this.slideCount = maxDepth(depthMap) + 1;

        // Create a stable ordering of tables included in this tree
        this.formIds = Lists.newArrayList(depthMap.keySet());
        formIds.sort(Ordering.natural().onResultOf(id -> id.asString()));
    }

    private static ResourceId findRootForm(FormTree formTree) {
        FormClass form = formTree.getFormClass(formTree.getRootFormId());
        while(form.isSubForm()) {
            ResourceId parentFormId = form.getParentFormId().get();
            FormClass parentForm = formTree.getFormClass(parentFormId);
            if(parentForm == null) {
                break;
            }
            form = parentForm;
        }
        return form.getId();
    }

    /**
     * Finds a path of forms, from the current, active form to the root form if it has any parents.
     */
    private static List<ResourceId> findFormPath(FormTree formTree, ResourceId activeFormId) {
        List<ResourceId> path = new ArrayList<>();
        FormClass form = formTree.getFormClass(activeFormId);
        path.add(form.getId());

        while(form.isSubForm()) {
            ResourceId parentFormId = form.getParentFormId().get();
            path.add(0, parentFormId);
            form = formTree.getFormClass(parentFormId);
        }
        return path;
    }

    /**
     * Find any subforms of the active form, recursively.
     */
    private static Map<ResourceId, Integer> collectSubForms(FormTree formTree, ResourceId rootFormId) {
        Map<ResourceId, Integer> depthMap = new HashMap<>();
        depthMap.put(rootFormId, 0);

        collectSubForms(formTree, rootFormId, depthMap, 1);
        return depthMap;
    }

    private static void collectSubForms(FormTree tree, ResourceId parentFormId, Map<ResourceId, Integer> depthMap, int depth) {
        FormClass parentSchema = tree.getFormClass(parentFormId);

        for (FormField field : parentSchema.getFields()) {
            if(field.getType() instanceof SubFormReferenceType) {
                SubFormReferenceType subFormType = (SubFormReferenceType) field.getType();
                ResourceId subFormId = subFormType.getClassId();
                if(tree.getFormClassIfPresent(subFormId).isPresent()) {
                    depthMap.put(subFormId, depth);
                    collectSubForms(tree, subFormId, depthMap, depth + 1);
                }
            }
        }
    }

    private int maxDepth(Map<ResourceId, Integer> depthMap) {
        Iterator<Integer> it = depthMap.values().iterator();
        int maxDepth = it.next();
        while(it.hasNext()) {
            int depth = it.next();
            if(depth > maxDepth) {
                maxDepth = depth;
            }
        }
        return maxDepth;
    }

    public ResourceId getRootFormId() {
        return rootFormId;
    }

    public int getSlideCount() {
        return slideCount;
    }

    public List<ResourceId> getFormIds() {
        return formIds;
    }

    public int getSlideIndex(ResourceId formId) {
        return depthMap.get(formId);
    }

    public List<ResourceId> findPathToRoot(ResourceId formId) {
        List<ResourceId> path = new ArrayList<>();
        path.add(formId);
        while(parentMap.containsKey(formId)) {
            formId = parentMap.get(formId);
            path.add(0, formId);
        }
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SliderTree that = (SliderTree) o;
        return Objects.equals(depthMap, that.depthMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depthMap);
    }

    public boolean isSubForm(ResourceId formId) {
        return depthMap.getOrDefault(formId, 0) > 0;
    }
}
