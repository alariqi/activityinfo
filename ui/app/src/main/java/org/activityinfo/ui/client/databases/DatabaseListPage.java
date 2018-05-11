package org.activityinfo.ui.client.databases;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.IconButton;
import org.activityinfo.ui.client.base.button.IconButtonStyle;
import org.activityinfo.ui.client.base.button.MenuButton;
import org.activityinfo.ui.client.base.toolbar.Toolbar;
import org.activityinfo.ui.client.page.PageContainer;

import java.util.List;

public class DatabaseListPage implements IsWidget {

    private final PageContainer container;
    private final StaticListView<ListItem> listView;

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

        listView = new StaticListView<>(new ListItemCell(), ListItem::getId);

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
