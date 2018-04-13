package org.activityinfo.ui.client.databases;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.theme.client.CssLayoutContainer;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.theme.client.StaticHtml;
import org.activityinfo.theme.client.base.button.IconButton;
import org.activityinfo.theme.client.base.button.MenuButton;

import java.util.List;

public class DatabaseListPage implements IsWidget {

    private final CssLayoutContainer outer;
    private final CssLayoutContainer body;
    private final CssLayoutContainer inner;
    private final StaticListView<ListItem> listView;

    public DatabaseListPage() {

        StaticHtml pageHeader = new StaticHtml(DatabaseTemplates.TEMPLATES.pageHeader(I18N.CONSTANTS.databases()));

        IconButton newDatabaseButton = new IconButton(Icon.BUBBLE_ADD, I18N.CONSTANTS.newDatabase());

        Menu sortMenu = new Menu();
        sortMenu.add(new MenuItem("Sort by recent use (recent first)"));
        sortMenu.add(new MenuItem("Sort alphabetically"));
        sortMenu.add(new MenuItem("Sort offline first"));

        MenuButton sortButton = new MenuButton("Sort by recent use (recent first)", sortMenu);

        CssLayoutContainer toolBar = new CssLayoutContainer();
        toolBar.addStyleName("listpage__toolbar");
        toolBar.add(newDatabaseButton);
        toolBar.add(sortButton);


        listView = new StaticListView<>(new ListItemCell(), ListItem::getId);

        inner = new CssLayoutContainer();
        inner.getElement().addClassName("listpage__body-inner");
        inner.add(toolBar);
        inner.add(listView);

        body = new CssLayoutContainer();
        body.getElement().addClassName("listpage__body");
        body.add(inner);

        outer = new CssLayoutContainer();
        outer.getElement().addClassName("listpage");
        outer.add(pageHeader);
        outer.add(body);
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
        return outer;
    }
}
