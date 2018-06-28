package org.activityinfo.ui.client.database;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.DatabaseHeader;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.avatar.GenericAvatar;
import org.activityinfo.ui.client.base.button.IconButton;
import org.activityinfo.ui.client.base.button.IconButtonStyle;
import org.activityinfo.ui.client.base.button.MenuButton;
import org.activityinfo.ui.client.base.listtable.ListItem;
import org.activityinfo.ui.client.base.listtable.ListItemCell;
import org.activityinfo.ui.client.base.listtable.ListTable;
import org.activityinfo.ui.client.base.listtable.StaticListTable;
import org.activityinfo.ui.client.base.toolbar.Toolbar;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.page.PageContainer;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.List;
import java.util.stream.Collectors;

public class DatabaseListPage implements IsWidget {

    private final PageContainer container;
    private final StaticListTable<ListItem> listView;


    public static VTree render(FormStore store) {

        Observable<List<ListItem>> databaseListItems = store.getDatabaseList().transform(list ->
                list.stream().map(d -> databaseListItem(d)).collect(Collectors.toList()));

        VTree databaseList = new ListTable(databaseListItems).render();

        return new PageBuilder()
                .padded()
                .heading(I18N.CONSTANTS.databases())
                .body(databaseList)
                .build();

    }

    private static ListItem databaseListItem(DatabaseHeader d) {
        return new ListItem(d.getDatabaseId().asString(),
                        d.getLabel(),
                        new DatabasePlace(d.getDatabaseId()),
                        GenericAvatar.DATABASE);
    }

    public DatabaseListPage() {

        IconButton newDatabaseButton = new IconButton(Icon.BUBBLE_ADD, IconButtonStyle.PRIMARY, I18N.CONSTANTS.newDatabase());

        Menu sortMenu = new Menu();
        sortMenu.add(new MenuItem("Sort by recent use (recent first)"));
        sortMenu.add(new MenuItem("Sort alphabetically"));
        sortMenu.add(new MenuItem("Sort offline first"));

        MenuButton sortButton = new MenuButton("Sort by recent use (recent first)", sortMenu);

        Toolbar toolbar = new Toolbar();
        toolbar.addAction(newDatabaseButton);
        toolbar.addSort(sortButton);

        listView = new StaticListTable<>(new ListItemCell(), ListItem::getId);

        container = new PageContainer();
        container.getHeader().setHeading(I18N.CONSTANTS.databases());
        container.addBodyWidget(toolbar);
        container.addBodyWidget(listView);
    }

    public void updateView(Observable<List<ListItem>> databases) {
        if(databases.isLoaded()) {
            listView.updateView(databases.get());
        } else {
            listView.clear();
        }
    }

    @Override
    public Widget asWidget() {
        return container.asWidget();
    }
}
