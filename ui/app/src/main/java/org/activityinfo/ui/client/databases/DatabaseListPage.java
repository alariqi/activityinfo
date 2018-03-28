package org.activityinfo.ui.client.databases;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.theme.client.*;
import org.activityinfo.ui.client.header.BreadcrumbBar;

import java.util.List;

public class DatabaseListPage implements IsWidget {

    private final VerticalLayoutContainer container;
    private final BreadcrumbBar breadcrumbs;
    private final ListStore<NavListItem> listStore;

    public DatabaseListPage() {

        breadcrumbs = new BreadcrumbBar();
        PageHeader pageHeader = new PageHeader(I18N.CONSTANTS.databases());

        ListToolBarButton newDatabaseButton = new ListToolBarButton(Icon.ADD, I18N.CONSTANTS.newDatabase());

        Menu sortMenu = new Menu();
        sortMenu.add(new MenuItem("Sort by recent use (recent first)"));
        sortMenu.add(new MenuItem("Sort alphabetically"));
        sortMenu.add(new MenuItem("Sort offline first"));

        TextButton sortButton = new TextButton("Sort by recent use (recent first)");
        sortButton.setMenu(sortMenu);

        ToolBar toolBar = new ToolBar();
        toolBar.add(newDatabaseButton);
        toolBar.add(new FillToolItem());
        toolBar.add(sortButton);

        listStore = new ListStore<>(NavListItem::getId);

        ListViewSelectionModel<NavListItem> selectionModel = new ListViewSelectionModel<>();
        selectionModel.setLocked(true);

        ListView<NavListItem, NavListItem> listView = new ListView<>(listStore,
                new IdentityValueProvider<>(),
                new NavListItemCell());

        listView.setSelectionModel(selectionModel);
        listView.setTrackMouseOver(false);

        container = new VerticalLayoutContainer();
        container.add(breadcrumbs, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(pageHeader, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(listView, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
    }

    public void updateView(Observable<List<NavListItem>> databases) {
        if(databases.isLoaded()) {
            listStore.replaceAll(databases.get());
        } else {
            listStore.clear();
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
