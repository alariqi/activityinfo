package org.activityinfo.ui.client.header;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.SplitButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.theme.client.Templates;
import org.activityinfo.ui.client.PlaceLinks;
import org.activityinfo.ui.client.databases.DatabaseListPlace;

public class BreadcrumbBar implements IsWidget {

    private final ToolBar toolBar;
    private final SubscriptionSet subscriptions = new SubscriptionSet();

    public BreadcrumbBar() {
        toolBar = new ToolBar();
        toolBar.setHeight(35);
    }

    public BreadcrumbBar(Observable<BreadcrumbViewModel> viewModel) {
        this();
        toolBar.addAttachHandler(event -> {
            if(event.isAttached()) {
                subscriptions.add(viewModel.subscribe(BreadcrumbBar.this::updateView));
            } else {
                subscriptions.unsubscribeAll();
            }
        });
    }


    private void updateView(Observable<BreadcrumbViewModel> viewModel) {
        if(viewModel.isLoaded()) {
            updateView(viewModel.get());
        }
    }

    public void updateView(BreadcrumbViewModel viewModel) {
        toolBar.clear();
        toolBar.add(new HTML(Templates.TEMPLATES.breadcrumb(I18N.CONSTANTS.databases(), PlaceLinks.toUri(new DatabaseListPlace()))));

        for (BreadcrumbViewModel.CrumbModel crumbModel : viewModel.getCrumbs()) {
            switch (crumbModel.getType()) {
                case DATABASE:
                    toolBar.add(databaseItem(crumbModel));
                    break;
                case FORM:
                    toolBar.add(formItem(crumbModel));
                    break;
            }
        }
    }

    private IsWidget databaseItem(BreadcrumbViewModel.CrumbModel crumbModel) {

        Menu databaseMenu = new Menu();
        databaseMenu.add(new MenuItem("Do something to the database"));
        databaseMenu.add(new MenuItem("Do something else..."));

        SplitButton button = new SplitButton(crumbModel.getLabel());
        button.setMenu(databaseMenu);

        return button;
    }


    private IsWidget formItem(BreadcrumbViewModel.CrumbModel crumbModel) {
        Menu formMenu = new Menu();
        formMenu.add(new MenuItem("Do something to the form"));
        formMenu.add(new MenuItem("Do something else..."));

        TextButton button = new TextButton(crumbModel.getLabel());
        button.setMenu(formMenu);

        return button;
    }


    @Override
    public Widget asWidget() {
        return toolBar;
    }
}
