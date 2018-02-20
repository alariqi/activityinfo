package chdc.frontend.client.table;

import chdc.frontend.client.i18n.ChdcLabels;
import chdc.frontend.client.theme.ActionBar;
import chdc.frontend.client.theme.Icon;
import chdc.frontend.client.theme.IconLinkButton;
import com.google.common.base.Optional;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import org.activityinfo.analysis.table.EffectiveTableModel;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.chrome.HasTitle;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.view.TableGrid;

import java.util.logging.Logger;

/**
 * Table view that occupies the full width, with an action bar below
 */
public class IncidentTableView implements IsWidget, HasTitle {

    private static final Logger LOGGER = Logger.getLogger(IncidentTableView.class.getName());

    private TableViewModel viewModel;
    private FormStore formStore;
    private TableGrid grid;
    private final BorderLayoutContainer container;
    private SubscriptionSet subscriptions = new SubscriptionSet();

    private final IconLinkButton editRowButton;

    public IncidentTableView(FormStore formStore, final TableViewModel viewModel) {
        this.formStore = formStore;

        this.viewModel = viewModel;

        // Action barToo

        ActionBar toolBar = new ActionBar();
        IconLinkButton newRow = new IconLinkButton(Icon.PLUS, ChdcLabels.LABELS.newRow(), UriUtils.fromTrustedString("#"));

        editRowButton = new IconLinkButton(Icon.PENCIL, I18N.CONSTANTS.edit(), UriUtils.fromTrustedString("#"));

        toolBar.addShortcut(newRow);
        toolBar.addShortcut(editRowButton);

        container = new BorderLayoutContainer();
        container.setSouthWidget(toolBar, new BorderLayoutContainer.BorderLayoutData(60));
        container.addAttachHandler(event -> {
            if(event.isAttached()) {
                subscriptions.add(viewModel.getEffectiveTable().subscribe(tm -> effectiveModelChanged()));
                subscriptions.add(viewModel.getSelectedRecordRef().subscribe(ref -> selectionChanged(ref)));

            } else {
                subscriptions.unsubscribeAll();
            }
        });
    }


    private void effectiveModelChanged() {
        if(viewModel.getEffectiveTable().isLoading()) {
            this.container.mask();
        } else {
            this.container.unmask();

            switch (viewModel.getEffectiveTable().get().getRootFormState()) {
                case FORBIDDEN:
                case DELETED:
                    showErrorState(viewModel.getEffectiveTable().get().getRootFormState());
                    break;
                case VALID:
                    updateGrid(viewModel.getEffectiveTable().get());
                    break;
            }
        }
    }

    private void selectionChanged(Observable<Optional<RecordRef>> ref) {

    }

    private void showErrorState(FormTree.State rootFormState) {

    }

    private void updateGrid(EffectiveTableModel effectiveTableModel) {

        // If the grid is already displayed, try to update without
        // destorying everything
        if(grid != null && grid.updateView(effectiveTableModel)) {
            return;
        }


        grid = new TableGrid(effectiveTableModel, viewModel.getColumnSet(), viewModel);
        grid.addSelectionChangedHandler(event -> {
            if(!event.getSelection().isEmpty()) {
                RecordRef ref = event.getSelection().get(0);
                viewModel.select(ref);
            }
        });
        container.setCenterWidget(grid);
        container.forceLayout();
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public Observable<String> getTitle() {
        throw new UnsupportedOperationException("TODO");
    }
}
