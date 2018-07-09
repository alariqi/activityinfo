package org.activityinfo.ui.client.table.viewModel;

import com.google.common.base.Optional;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.model.TableModel;
import org.activityinfo.ui.client.table.model.TableSliderModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableSliderViewModel {
    private final SliderTree sliderTree;
    private final Observable<FormTree> formTree;
    private final Observable<List<Breadcrumb>> breadcrumbs;
    private final Observable<TablePlace> place;
    private final Observable<String> title;
    private final List<TableViewModel> tables = new ArrayList<>();
    private final Observable<SliderPos> position;
    private final Observable<Boolean> inputVisible;
    private final Observable<java.util.Optional<FormInputModel>> inputModel;

    public static Observable<Maybe<TableSliderViewModel>> compute(FormStore formStore, Observable<TableSliderModel> state) {

        // Recompute the root TableSliderViewModel only if we navigate to a completely different
        // tree of forms, or if permissions change.

        Observable<TablePlace> place = state.transform(s -> s.getPlace()).cache();
        Observable<Maybe<FormTree>> formTree = place.join(p -> formStore.getFormTree(p.getFormId())).transform(FormTree::toMaybe);
        Observable<Maybe<SliderTree>> sliderTree = formTree.transform(maybe -> maybe.transform(SliderTree::new));

        return sliderTree.cache().transform(maybe -> maybe.transform(st -> new TableSliderViewModel(formStore, st, state)));
    }

    private TableSliderViewModel(FormStore formStore, SliderTree tree, Observable<TableSliderModel> model) {

        this.sliderTree = tree;
        this.formTree = formStore.getFormTree(sliderTree.getRootFormId());
        this.place = model.transform(m -> m.getPlace()).cache();
        this.position = this.place.transform(p -> new SliderPos(sliderTree, p));

        for (ResourceId formId : sliderTree.getFormIds()) {
            Observable<TableModel> tableState = model.transform(m -> m.getTable(formId));
            TableViewModel tableViewModel = new TableViewModel(formStore, sliderTree, formId, tableState, place);

            tables.add(tableViewModel);
        }

        // Breadcrumbs and title also change based on edit status
        this.title = computeTitle(formTree, model);
        this.breadcrumbs = computeBreadcrumbs(formStore, formTree, model);


        this.inputModel = model.transform(m -> m.getInput()).cache();
        this.inputVisible = inputModel.transform(m -> m.isPresent()).cache();
    }

    private static Observable<String> computeTitle(Observable<FormTree> formTree, Observable<TableSliderModel> model) {
        Observable<ResourceId> formId = model.transform(m -> m.getPlace().getFormId()).cache();
        return Observable.transform(formTree, formId, (tree, id) -> {
            FormClass formClass = tree.getFormClass(id);
            if(formClass == null) {
                return "";
            } else {
                return formClass.getLabel();
            }
        });
    }

    public Observable<String> getPageTitle() {
        return title;
    }

    private static Observable<List<Breadcrumb>> computeBreadcrumbs(FormStore formStore, Observable<FormTree> formTree, Observable<TableSliderModel> model) {

        return Observable.join(formTree, model, (tree, m) -> {

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
        });
    }

    public Observable<List<Breadcrumb>> getBreadcrumbs() {
        return breadcrumbs;
    }

    public Observable<SliderPos> getSliderPosition() {
        return position;
    }

    public SliderTree getTree() {
        return sliderTree;
    }

    public List<TableViewModel> getTables() {
        return tables;
    }

    public int getSlideCount() {
        return sliderTree.getSlideCount();
    }

    public Observable<java.util.Optional<FormInputModel>> getInputModel() {
        return inputModel;
    }

    public Observable<Boolean> isInputVisible() {
        return inputVisible;
    }
}