package org.activityinfo.ui.client.database;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.theme.client.Icon;
import org.activityinfo.theme.client.ListToolBarButton;
import org.activityinfo.ui.client.databases.ListItem;
import org.activityinfo.ui.client.databases.ListItemCell;

public class DatabasePage implements IsWidget {

    private final HtmlLayoutContainer container;
    private final ListStore<ListItem> listStore;

    public DatabasePage() {

        ListToolBarButton newFormButton = new ListToolBarButton(Icon.ADD, I18N.CONSTANTS.newForm());
        ListToolBarButton newFolderButton = new ListToolBarButton(Icon.ADD, I18N.CONSTANTS.newFolder());

        Menu sortMenu = new Menu();
        sortMenu.add(new MenuItem("Sort by recent use (recent first)"));
        sortMenu.add(new MenuItem("Sort alphabetically"));
        sortMenu.add(new MenuItem("Sort offline first"));

        TextButton sortButton = new TextButton("Sort by recent use (recent first)");
        sortButton.setMenu(sortMenu);

        listStore = new ListStore<>(ListItem::getId);

        ListViewSelectionModel<ListItem> selectionModel = new ListViewSelectionModel<>();
        selectionModel.setLocked(true);

        ListView<ListItem, ListItem> listView = new ListView<>(listStore,
                new IdentityValueProvider<>(),
                new ListItemCell());

        listView.setSelectionModel(selectionModel);
        listView.setTrackMouseOver(false);

        container = new HtmlLayoutContainer(DatabaseTemplates.TEMPLATES.page(I18N.CONSTANTS));
        container.add(newFormButton, new HtmlData(".listpage__toolbar"));
        container.add(newFolderButton, new HtmlData(".listpage__toolbar"));
        container.add(sortButton, new HtmlData(".listpage__toolbar"));
        container.add(listView, new HtmlData(".listpage__body-inner"));
    }

    public void updateView(Observable<DatabaseViewModel> viewModel) {
        if(viewModel.isLoaded()) {
            // Update header
            container.getElement().getElementsByTagName("h2").getItem(0).setInnerText(viewModel.get().getLabel());

            // Update list of forms
            listStore.replaceAll(viewModel.get().getFormLinks());


            // Update breadcrumbs
            XElement breadcrumbContainer = container.getElement().selectNode(".breadcrumbs");
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
