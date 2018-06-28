package org.activityinfo.ui.client.table.viewModel;

import com.google.common.base.Optional;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.subform.SubFormReferenceType;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.model.TableModelStore;
import org.activityinfo.ui.client.table.model.TableSliderModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableSliderViewModel {


    private final FormTree formTree;
    private final TablePlace place;
    private final Observable<List<Breadcrumb>> breadcrumbs;
    private final List<TableViewModel> tables = new ArrayList<>();
    private final int slideIndex;
    private final int slideCount;

    public static Observable<Maybe<TableSliderViewModel>> compute(
            FormStore formStore,
            TableModelStore modelStore,
            TablePlace tablePlace) {

        Observable<FormTree> formTree = formStore.getFormTree(tablePlace.getRootFormId());
        Observable<TableSliderModel> model = modelStore.getTableTreeModel(tablePlace.getRootFormId());

        return Observable.transform(formTree, model, (maybeTree, m) -> {
            return maybeTree.toMaybe().transform(tree -> {
                return new TableSliderViewModel(formStore, tree, m, tablePlace, computeBreadcrumbs(formStore, tree, tablePlace));
            });
        });
    }

    private TableSliderViewModel(FormStore formStore, FormTree tree, TableSliderModel model, TablePlace tablePlace, Observable<List<Breadcrumb>> breadcrumbs) {
        this.formTree = tree;
        this.place = tablePlace;
        this.breadcrumbs = breadcrumbs;

        // Find the path from this subform to the root form.
        // Tables on this path will be visible so that if we navigate to the left
        // they will appear in the transition
        List<ResourceId> path = findFormPath(formTree, tablePlace.getActiveFormId());

        this.slideIndex = path.size() - 1;

        TableModelStore modelStore = TableModelStore.STORE;

        this.tables.add(new TableViewModel(formStore, tree, formTree.getRootFormId(), modelStore.getTableModel(formTree.getRootFormClass().getId()), true, 0));

        // find subforms
        // TODO: actually support multi-level subforms
        for (FormTree.Node node : tree.getRootFields()) {
            if(node.isVisibleSubForm()) {
                SubFormReferenceType subFormType = (SubFormReferenceType) node.getType();
                ResourceId subFormId = subFormType.getClassId();
                FormTree subTree = tree.subTree(subFormId);
                boolean visible = path.contains(subFormId);
                int depth = 1;

                this.tables.add(new TableViewModel(formStore, subTree, formTree.getRootFormId(), modelStore.getTableModel(subFormId), visible, depth));
            }
        }

        this.slideCount = (tables.size() == 1) ? 1 : 2;
    }

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

    public String getPageTitle() {
        return place.switchCase(new TablePlace.Case<String>() {
            @Override
            public String rootTable(ResourceId formId) {
                return formTree.getRootFormClass().getLabel();
            }

            @Override
            public String subFormTable(ResourceId formId, ResourceId subFormId, RecordRef parentRef) {
                return formTree.getFormClass(subFormId).getLabel();
            }

            @Override
            public String editForm(ResourceId formId, RecordRef ref) {
                return I18N.CONSTANTS.addRecord();
            }
        });
    }

    private static Observable<List<Breadcrumb>> computeBreadcrumbs(FormStore formStore, FormTree tree, TablePlace place) {

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