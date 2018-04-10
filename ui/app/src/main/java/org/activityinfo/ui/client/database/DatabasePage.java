package org.activityinfo.ui.client.database;

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
import org.activityinfo.theme.client.Icon;
import org.activityinfo.theme.client.ListToolBarButton;
import org.activityinfo.theme.client.PageHeader;
import org.activityinfo.ui.client.databases.ListItem;
import org.activityinfo.ui.client.databases.ListItemCell;
import org.activityinfo.ui.client.header.BreadcrumbBar;
import org.activityinfo.ui.client.header.BreadcrumbViewModel;

public class DatabasePage implements IsWidget {

    private final VerticalLayoutContainer container;
    private final BreadcrumbBar breadcrumbs;
    private final ListStore<ListItem> listStore;
    private final PageHeader pageHeader;

    public DatabasePage() {

        breadcrumbs = new BreadcrumbBar();
        pageHeader = new PageHeader(I18N.CONSTANTS.loading());

        ListToolBarButton newFormButton = new ListToolBarButton(Icon.ADD, I18N.CONSTANTS.newForm());
        ListToolBarButton newFolderButton = new ListToolBarButton(Icon.ADD, I18N.CONSTANTS.newFolder());

        Menu sortMenu = new Menu();
        sortMenu.add(new MenuItem("Sort by recent use (recent first)"));
        sortMenu.add(new MenuItem("Sort alphabetically"));
        sortMenu.add(new MenuItem("Sort offline first"));

        TextButton sortButton = new TextButton("Sort by recent use (recent first)");
        sortButton.setMenu(sortMenu);

        ToolBar toolBar = new ToolBar();
        toolBar.add(newFormButton);
        toolBar.add(newFolderButton);
        toolBar.add(new FillToolItem());
        toolBar.add(sortButton);

        listStore = new ListStore<>(ListItem::getId);

        ListViewSelectionModel<ListItem> selectionModel = new ListViewSelectionModel<>();
        selectionModel.setLocked(true);

        ListView<ListItem, ListItem> listView = new ListView<>(listStore,
                new IdentityValueProvider<>(),
                new ListItemCell());

        listView.setSelectionModel(selectionModel);
        listView.setTrackMouseOver(false);

        container = new VerticalLayoutContainer();
        container.add(breadcrumbs, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(pageHeader, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(listView, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
    }

    public void updateView(Observable<DatabaseViewModel> viewModel) {
        if(viewModel.isLoaded()) {
            pageHeader.setHeading(viewModel.get().getLabel());
            breadcrumbs.updateView(BreadcrumbViewModel.forDatabase(viewModel.get().getDatabase()));

            listStore.replaceAll(viewModel.get().getFormLinks());
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
