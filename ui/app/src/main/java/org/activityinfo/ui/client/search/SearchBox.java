package org.activityinfo.ui.client.search;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.observable.Observable;
import org.activityinfo.theme.client.ClassNames;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.folder.FolderPlace;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePlace;

import java.util.ArrayList;
import java.util.List;

public class SearchBox implements IsWidget {

    private final ComboBox<SearchResult> comboBox;
    private final ListStore<SearchResult> store;


    public SearchBox(FormStore formStore) {
        store = new ListStore<>(SearchResult::getKey);

        comboBox = new ComboBox<>(new SearchComboBoxCell(store));
        comboBox.setEmptyText(I18N.CONSTANTS.searchPlaceholder());
        comboBox.addStyleName(ClassNames.SEARCH);
        comboBox.setWidth(-1);

        comboBox.addBeforeSelectionHandler(event -> {
            comboBox.setText(null);
            navigate(event.getItem());
            event.cancel();
        });

        // Keep the list of search options ready at all times
        formStore.getDatabases().subscribe(observable -> indexForms(observable));
    }

    private void indexForms(Observable<List<UserDatabaseMeta>> observable) {
        if(observable.isLoaded()) {
            List<SearchResult> results = new ArrayList<>();
            for (UserDatabaseMeta database : observable.get()) {
                results.add(new SearchResult(database));
                for (Resource resource : database.getResources()) {
                    results.add(new SearchResult(database, resource));
                }
            }
            store.replaceAll(results);
        }
    }

    private void navigate(SearchResult item) {
        switch (item.getResourceType()) {
            case FORM:
                History.newItem(new TablePlace(item.getId()).toString());
                break;
            case DATABASE:
                History.newItem(new DatabasePlace(item.getId()).toString());
                break;
            case FOLDER:
                History.newItem(new FolderPlace(item.getId()).toString());
                break;
        }
    }

    @Override
    public Widget asWidget() {
        return comboBox;
    }
}
