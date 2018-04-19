package org.activityinfo.ui.client.database;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.theme.client.base.button.IconButton;
import org.activityinfo.theme.client.base.button.IconButtonStyle;
import org.activityinfo.theme.client.base.button.MenuButton;
import org.activityinfo.ui.client.databases.ListItem;
import org.activityinfo.ui.client.databases.ListItemCell;
import org.activityinfo.ui.client.databases.StaticListView;

public class DatabasePage implements IsWidget {

    private final HtmlLayoutContainer container;
    private final StaticListView<ListItem> listView;

    public DatabasePage() {

        IconButton newFormButton = new IconButton(Icon.BUBBLE_ADD, IconButtonStyle.PRIMARY, I18N.CONSTANTS.newForm());
        IconButton newFolderButton = new IconButton(Icon.BUBBLE_ADD, I18N.CONSTANTS.newFolder());

        Menu sortMenu = new Menu();
        sortMenu.add(new MenuItem("Sort by recent use (recent first)"));
        sortMenu.add(new MenuItem("Sort alphabetically"));
        sortMenu.add(new MenuItem("Sort offline first"));

        MenuButton sortButton = new MenuButton("Sort by recent use (recent first)", sortMenu);

        listView = new StaticListView<>(new ListItemCell(), ListItem::getId);
        listView.addStyleName("listview--forms");

        container = new HtmlLayoutContainer(DatabaseTemplates.TEMPLATES.page(I18N.CONSTANTS));
        container.add(newFormButton, new HtmlData(".listpage__toolbar__actions"));
        container.add(newFolderButton, new HtmlData(".listpage__toolbar__actions"));
        container.add(sortButton, new HtmlData(".listpage__toolbar"));
        container.add(listView, new HtmlData(".listpage__body__inner"));
    }

    public void updateView(Observable<DatabaseViewModel> viewModel) {
        if(viewModel.isLoaded()) {
            // Update header
            container.getElement().getElementsByTagName("h1").getItem(0).setInnerText(viewModel.get().getLabel());

            // Update list of forms
            listView.updateView(viewModel.get().getFormLinks());

        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
