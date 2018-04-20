package org.activityinfo.ui.client.table;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.view.TableView;

public class TablePage implements IsWidget {

    private final VerticalLayoutContainer container;

    public TablePage(FormStore formStore, TableViewModel viewModel) {


        TableView tableView = new TableView(formStore, viewModel);

        container = new VerticalLayoutContainer();
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
//            pageHeader.setHeading(formTree.get().getRootFormClass().getLabel());
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
