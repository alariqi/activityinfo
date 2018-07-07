package org.activityinfo.ui.client.database;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.DatabaseHeader;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.AppFrame;
import org.activityinfo.ui.client.Page;
import org.activityinfo.ui.client.base.avatar.GenericAvatar;
import org.activityinfo.ui.client.base.listtable.ListItem;
import org.activityinfo.ui.client.base.listtable.ListTable;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.List;
import java.util.stream.Collectors;

public class DatabaseListPage extends Page {

    private final FormStore store;

    public DatabaseListPage(FormStore store) {
        this.store = store;
    }

    @Override
    public VTree render() {

        Observable<List<ListItem>> databaseListItems = store.getDatabaseList().transform(list ->
                list.stream().map(d -> databaseListItem(d)).collect(Collectors.toList()));

        VTree databaseList = new ListTable(databaseListItems).render();

        VTree page = new PageBuilder()
                .padded()
                .heading(I18N.CONSTANTS.databases())
                .body(databaseList)
                .build();

        return AppFrame.render(store, page);
    }

    private static ListItem databaseListItem(DatabaseHeader d) {
        return new ListItem(d.getDatabaseId().asString(),
                        d.getLabel(),
                        new DatabasePlace(d.getDatabaseId()),
                        GenericAvatar.DATABASE);
    }
}
