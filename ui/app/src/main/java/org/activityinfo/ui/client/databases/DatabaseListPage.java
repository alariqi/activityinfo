package org.activityinfo.ui.client.databases;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.theme.client.ListToolBarButton;
import org.activityinfo.theme.client.NavListItem;
import org.activityinfo.theme.client.PageHeader;
import org.activityinfo.ui.client.header.BreadcrumbBar;

public class DatabaseListPage implements IsWidget {

    private final VerticalLayoutContainer container;
    private final BreadcrumbBar breadcrumbs;

    public DatabaseListPage() {

        breadcrumbs = new BreadcrumbBar();
        PageHeader pageHeader = new PageHeader(I18N.CONSTANTS.database());

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

        ListStore<UserDatabaseMeta> store = new ListStore<>(d -> d.getDatabaseId().asString());
        ListView<UserDatabaseMeta, NavListItem> listView = new ListView<>(store, new DatabaseValueProvider());
        for (int i = 0; i < 25; i++) {
            store.add(new UserDatabaseMeta.Builder()
            .setDatabaseId(i)
            .setLabel("CHMP Database #" + i)
            .setOwner(true)
            .build());
        }

        container = new VerticalLayoutContainer();
        container.add(breadcrumbs, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(pageHeader, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(listView, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
