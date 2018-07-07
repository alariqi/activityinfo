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
import org.activityinfo.ui.client.base.ClassNames;
import org.activityinfo.ui.client.database.DatabasePlace;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePlace;

import java.util.ArrayList;
import java.util.List;

public class SearchBox implements IsWidget {

    private final ComboBox<SearchResult> comboBox;
    private final ListStore<SearchResult> store;

    public SearchBox(FormStore formStore) {
        this(resourceList(formStore));
    }

    public SearchBox(Observable<List<SearchResult>> resourceList) {
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

        // Start loading the list when the box is focused
        comboBox.addFocusHandler(event -> resourceList.subscribe(observable -> {
            if(observable.isLoaded()) {
                store.replaceAll(observable.get());
            }
        }));
    }

    private static Observable<List<SearchResult>> resourceList(FormStore formStore) {
        return formStore.getDatabases().transform(databases -> {
            List<SearchResult> results = new ArrayList<>();
            for (UserDatabaseMeta database : databases) {
                results.add(new SearchResult(database));
                for (Resource resource : database.getResources()) {
                    results.add(new SearchResult(database, resource));
                }
            }
            return results;
        });
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
                History.newItem(new DatabasePlace(item.getDatabaseId(), item.getId()).toString());
                break;
        }
    }

    @Override
    public Widget asWidget() {
        return comboBox;
    }
}
