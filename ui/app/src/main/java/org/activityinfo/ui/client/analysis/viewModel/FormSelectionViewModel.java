package org.activityinfo.ui.client.analysis.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.analysis.model.FormSelectionModel;
import org.activityinfo.ui.client.store.FormStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FormSelectionViewModel {

    private final List<Observable<FormSelectionColumn>> columns;

    public FormSelectionViewModel(List<Observable<FormSelectionColumn>> columns) {
        this.columns = columns;
    }

    public List<Observable<FormSelectionColumn>> getColumns() {
        return columns;
    }

    public static FormSelectionViewModel compute(FormStore formStore, FormSelectionModel model) {

        List<Observable<FormSelectionColumn>> columns = new ArrayList<>();

        FormSelectionColumn root = new FormSelectionColumn(0,
                Arrays.asList(
                    new FormSelectionItem(FormSelectionModel.DATABASE_ROOT_ID, I18N.CONSTANTS.databases(), FormSelectionItem.Selection.NONE),
                    new FormSelectionItem(FormSelectionModel.REPORTS_ROOT_ID, I18N.CONSTANTS.reports(), FormSelectionItem.Selection.NONE)),
                        selection(model.getNavigationPath(), 0));

        columns.add(Observable.just(root));

        if(model.getNavigationPath().size() >= 1 &&
                model.getNavigationPath().get(0).equals(FormSelectionModel.DATABASE_ROOT_ID)) {

            databaseColumns(formStore, model, columns);

        }

        return new FormSelectionViewModel(columns);
    }

    private static void databaseColumns(FormStore formStore, FormSelectionModel model, List<Observable<FormSelectionColumn>> columns) {

        // If we have navigated to databases, the next column is a list of all databases.

        Observable<FormSelectionColumn> databasesColumn = formStore.getDatabases().transform(list -> {
            List<FormSelectionItem> items = list
                    .stream()
                    .map(d -> new FormSelectionItem(d.getDatabaseId(), d.getLabel(),
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

    private static FormSelectionColumn databaseColumn(FormSelectionModel model, Maybe<UserDatabaseMeta> d, int columnIndex) {
        ResourceId parentId = model.getNavigationPath().get(columnIndex - 1);

        List<FormSelectionItem> items = new ArrayList<>();
        if(d.isVisible()) {
            for (Resource resource : d.get().getResources()) {
                if(resource.getParentId().equals(parentId)) {
                    items.add(new FormSelectionItem(resource.getId(), resource.getLabel(),
                            model.isSelected(resource.getId()) ?
                                    FormSelectionItem.Selection.ALL :
                                    FormSelectionItem.Selection.NONE));
                }
            }
        }

        return new FormSelectionColumn(columnIndex, items, model.getCurrent(columnIndex));
    }

    private static Optional<ResourceId> selection(List<ResourceId> selectionPath, int columnIndex) {
        if(selectionPath.size() > columnIndex) {
            return Optional.of(selectionPath.get(columnIndex));
        } else {
            return Optional.empty();
        }
    }
}