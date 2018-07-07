package org.activityinfo.ui.client.table.viewModel;

import com.google.common.base.Optional;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.subform.SubFormReferenceType;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.model.TableModel;
import org.activityinfo.ui.client.table.model.TableSliderModel;

import java.util.*;

public class TableSliderViewModel {
    private final FormTree formTree;
    private final TablePlace place;
    private final Observable<List<Breadcrumb>> breadcrumbs;
    private final List<TableViewModel> tables = new ArrayList<>();
    private final int slideIndex;
    private final int slideCount;

    public static Observable<Maybe<TableSliderViewModel>> compute(
            FormStore formStore,
            Observable<TableSliderModel> model) {

        return model.join(m -> formStore.getFormTree(m.getPlace().getFormId()).transform(
                  maybe -> maybe.toMaybe().transform(
                  tree -> new TableSliderViewModel(formStore, tree, m))));
    }

    private TableSliderViewModel(FormStore formStore, FormTree tree, TableSliderModel model) {
        this.formTree = tree;
        this.place = model.getPlace();
        this.breadcrumbs = computeBreadcrumbs(formStore, tree);

        // Find the path from this subform to the root form.
        // Tables on this path will be visible so that if we navigate to the left
        // they will appear in the transition
        List<ResourceId> path = findFormPath(formTree, place.getFormId());

        // Find all the sub(forms) in this hierarchy and their depth relative to the root form
        Map<ResourceId, Integer> depthMap = collectSubForms(formTree, path.get(0));

        // Keep track of parents of forms
        // TODO: parents of parents...?
        Map<ResourceId, String> parentMap = new HashMap<>();
        place.getParentId().ifPresent(parentId -> {
            parentMap.put(place.getFormId(), parentId);
        });

        // Add a view model for each table visited so far
        for (TableModel tableModel : model.getTables()) {
            ResourceId formId = tableModel.getFormId();
            int depth = depthMap.get(formId);
            boolean visible = path.contains(formId);
            FormTree relativeTree = tree.subTree(formId);
            java.util.Optional<String> parentRecordId = java.util.Optional.ofNullable(parentMap.get(formId));

            this.tables.add(new TableViewModel(formStore, relativeTree, tableModel, visible, depth, parentRecordId));
        }

        // The total number of "slides" is equal to the maximum depth of the hierarchy
        // and our current slide index is equal to the active form's position in the path
        this.slideCount = maxDepth(depthMap) + 1;
        this.slideIndex = path.indexOf(place.getFormId());

    }


    /**
     * Finds a path of forms, from the current, active form to the root form if it has any parents.
     */
    private List<ResourceId> findFormPath(FormTree formTree, ResourceId activeFormId) {
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
    private Map<ResourceId, Integer> collectSubForms(FormTree formTree, ResourceId rootFormId) {
        Map<ResourceId, Integer> depthMap = new HashMap<>();
        depthMap.put(rootFormId, 0);

        collectSubForms(formTree, rootFormId, depthMap, 1);
        return depthMap;
    }

    private void collectSubForms(FormTree tree, ResourceId parentFormId, Map<ResourceId, Integer> depthMap, int depth) {
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

    public String getPageTitle() {
        return formTree.getFormClass(place.getFormId()).getLabel();
    }

    private static Observable<List<Breadcrumb>> computeBreadcrumbs(FormStore formStore, FormTree tree) {

        ResourceId formId = tree.getRootFormId();
        ResourceId databaseId = tree.getRootFormClass().getDatabaseId();
        String formLabel = tree.getRootFormClass().getLabel();

        // Define a partial breadcrumb trail while we are loading the database metadata needed for the
        // full breadcrumb
        List<Breadcrumb> loading = Arrays.asList(Breadcrumb.DATABASES,
                Breadcrumb.loadingPlaceholder("Database"),
                new Breadcrumb(formLabel, new TablePlace(formId)));

        Observable<Optional<List<Breadcrumb>>> breadcrumbs = formStore.getDatabase(databaseId).transform(maybeDatabase -> {
            return maybeDatabase.getIfVisible().transform(database -> {
                return Breadcrumb.hierarchy(database, formId);
            });
        });

        // Breadcrumbs will only be fully available if the database is also visible. Since we know that the
        // database will be visible to the user if this form is visible to the user, which is already loaded,
        // treat a (forbidden/not found) database as loading
        return Observable.flattenOptional(breadcrumbs).optimisticWithDefault(loading);
    }

    public Observable<List<Breadcrumb>> getBreadcrumbs() {
        return breadcrumbs;
    }

    public List<TableViewModel> getTables() {
        return tables;
    }

    /**
     * @return the current active slide index
     */
    public int getSlideIndex() {
        return slideIndex;
    }

    public int getSlideCount() {
        return slideCount;
    }
}