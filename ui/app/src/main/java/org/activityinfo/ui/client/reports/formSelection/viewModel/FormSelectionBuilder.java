package org.activityinfo.ui.client.reports.formSelection.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.reports.formSelection.state.FormSelectionState;
import org.activityinfo.ui.client.store.FormStore;

import java.util.*;
import java.util.stream.Collectors;

public class FormSelectionBuilder {


    public static FormSelectionViewModel compute(FormStore formStore, FormSelectionState model) {

        List<Observable<FormSelectionColumn>> columns = new ArrayList<>();

        FormSelectionColumn root = new FormSelectionColumn(0,
                Arrays.asList(
                    new FormSelectionItem(FormSelectionState.DATABASE_ROOT_ID, FormSelectionItem.ItemType.ROOT, I18N.CONSTANTS.databases(), FormSelectionItem.Selection.NONE),
                    new FormSelectionItem(FormSelectionState.REPORTS_ROOT_ID, FormSelectionItem.ItemType.ROOT, I18N.CONSTANTS.reports(), FormSelectionItem.Selection.NONE)),
                        selection(model.getNavigationPath(), 0));

        columns.add(Observable.just(root));

        if(model.getNavigationPath().size() >= 1 &&
                model.getNavigationPath().get(0).equals(FormSelectionState.DATABASE_ROOT_ID)) {

            databaseColumns(formStore, model, columns);
        }


        return new FormSelectionViewModel(columns);
    }

    private static void databaseColumns(FormStore formStore, FormSelectionState model, List<Observable<FormSelectionColumn>> columns) {

        // If we have navigated to databases, the next column is a list of all databases.

        Observable<FormSelectionColumn> databasesColumn = formStore.getDatabases().transform(list -> {
            List<FormSelectionItem> items = list
                    .stream()
                    .map(d -> new FormSelectionItem(d.getDatabaseId(), FormSelectionItem.ItemType.DATABASE, d.getLabel(),
                            model.isSelected(d.getDatabaseId()) ?
                            FormSelectionItem.Selection.ALL :
                            FormSelectionItem.Selection.NONE))
                    .collect(Collectors.toList());

            return new FormSelectionColumn(1, items,
                    selection(model.getNavigationPath(), 1));
        });
        columns.add(databasesColumn);

        // Is there a database selected?

        if (model.getNavigationPath().size() < 2) {
            return;
        }
        ResourceId databaseId = model.getNavigationPath().get(1);
        Observable<Maybe<UserDatabaseMeta>> database = formStore.getDatabase(databaseId);

        for (int i = 2; i < model.getNavigationPath().size() + 1; i++) {
            final int columnIndex = i;
            columns.add(database.transform(d -> databaseColumn(model, d, columnIndex)));
        }
    }

    private static FormSelectionColumn databaseColumn(FormSelectionState model, Maybe<UserDatabaseMeta> d, int columnIndex) {
        ResourceId parentId = model.getNavigationPath().get(columnIndex - 1);

        List<FormSelectionItem> items = new ArrayList<>();
        if(d.isVisible()) {
            for (Resource resource : d.get().getResources()) {
                if(resource.getParentId().equals(parentId)) {

                    FormSelectionItem.ItemType itemType = itemTypeOf(resource);

                    items.add(new FormSelectionItem(resource.getId(), itemType, resource.getLabel(),
                            model.isSelected(resource.getId()) ?
                                    FormSelectionItem.Selection.ALL :
                                    FormSelectionItem.Selection.NONE));
                }
            }
        }

        items.sort(Comparator.naturalOrder());

        return new FormSelectionColumn(columnIndex, items, model.getCurrent(columnIndex));
    }

    private static FormSelectionItem.ItemType itemTypeOf(Resource resource) {
        switch (resource.getType()) {
            case FOLDER:
                return FormSelectionItem.ItemType.FOLDER;
            default:
                return FormSelectionItem.ItemType.FORM;
        }
    }

    private static Optional<ResourceId> selection(List<ResourceId> selectionPath, int columnIndex) {
        if(selectionPath.size() > columnIndex) {
            return Optional.of(selectionPath.get(columnIndex));
        } else {
            return Optional.empty();
        }
    }
}
