package org.activityinfo.ui.client.table;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.theme.client.PageHeader;
import org.activityinfo.ui.client.header.BreadcrumbBar;
import org.activityinfo.ui.client.header.BreadcrumbViewModel;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.view.TableView;

public class TablePage implements IsWidget {

    private final VerticalLayoutContainer container;
    private final BreadcrumbBar breadcrumbs;
    private final PageHeader pageHeader;

    public TablePage(FormStore formStore, TableViewModel viewModel) {

        breadcrumbs = new BreadcrumbBar(BreadcrumbViewModel.forForm(formStore, viewModel.getFormId()));
        pageHeader = new PageHeader(I18N.CONSTANTS.loading());

        TableView tableView = new TableView(formStore, viewModel);

        container = new VerticalLayoutContainer();
        container.add(breadcrumbs, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(pageHeader, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        container.add(tableView, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

        SubscriptionSet subscriptions = new SubscriptionSet();
        container.addAttachHandler(event -> {
            if(event.isAttached()) {
                subscriptions.add(viewModel.getFormTree().subscribe(this::updateHeader));
            } else {
                subscriptions.unsubscribeAll();
            }
        });
    }

    private void updateHeader(Observable<FormTree> formTree) {
        if(formTree.isLoaded() && formTree.get().getRootState() == FormTree.State.VALID) {
            pageHeader.setHeading(formTree.get().getRootFormClass().getLabel());
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
