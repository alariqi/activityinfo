package org.activityinfo.ui.client.database;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.IconButton;
import org.activityinfo.ui.client.base.button.IconButtonStyle;
import org.activityinfo.ui.client.base.button.MenuButton;
import org.activityinfo.ui.client.base.container.StaticHtml;
import org.activityinfo.ui.client.base.toolbar.Toolbar;
import org.activityinfo.ui.client.nonideal.ViewWidget;
import org.activityinfo.ui.client.page.Breadcrumb;
import org.activityinfo.ui.client.page.GenericAvatar;
import org.activityinfo.ui.client.page.PageContainer;

public class DatabasePage implements ViewWidget<DatabaseViewModel> {

    private final PageContainer container;
    private final StaticListView<ListItem> listView;


    public DatabasePage() {

        IconButton newFormButton = new IconButton(Icon.BUBBLE_ADD, IconButtonStyle.PRIMARY, I18N.CONSTANTS.newForm());
        IconButton newFolderButton = new IconButton(Icon.BUBBLE_ADD, I18N.CONSTANTS.newFolder());

        Menu sortMenu = new Menu();
        sortMenu.add(new MenuItem("Sort by recent use (recent first)"));
        sortMenu.add(new MenuItem("Sort alphabetically"));
        sortMenu.add(new MenuItem("Sort offline first"));

        MenuButton sortButton = new MenuButton("Sort by recent use (recent first)", sortMenu);

        Toolbar toolbar = new Toolbar();
        toolbar.addAction(newFormButton);
        toolbar.addAction(newFolderButton);
        toolbar.addSort(sortButton);

        listView = new StaticListView<>(new ListItemCell(), ListItem::getId);
        listView.addStyleName("listview--forms");

        container = new PageContainer();
        container.getHeader().setAvatar(GenericAvatar.DATABASE);
        container.addBodyWidget(new StaticHtml(DatabaseTemplates.TEMPLATES.header(I18N.CONSTANTS.forms())));
        container.addBodyWidget(toolbar);
        container.addBodyWidget(listView);
        container.setBreadcrumbs(Breadcrumb.DATABASES);
    }

    @Override
    public void updateView(DatabaseViewModel viewModel) {
        // Update header
        container.getHeader().setHeading(viewModel.getLabel());
        container.getHeader().setBreadcrumbs(Breadcrumb.DATABASES, Breadcrumb.of(viewModel.getDatabase()));

        // Update list of forms
        listView.updateView(viewModel.getFormLinks());
    }

    @Override
    public void setVisible(boolean visible) {
        container.setVisible(visible);
    }

    @Override
    public Widget asWidget() {
        return container.asWidget();
    }
}
