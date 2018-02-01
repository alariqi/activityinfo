package chdc.frontend.client.table;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.model.analysis.ImmutableTableModel;
import org.activityinfo.model.analysis.TableModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.store.FormStore;

public class TableActivity implements Activity {


    private final FormStore formStore;
    private final TablePlace place;

    public TableActivity(FormStore formStore, TablePlace place) {
        this.formStore = formStore;
        this.place = place;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        TableModel model = ImmutableTableModel.builder()
                .formId(ResourceId.valueOf(place.getFormId()))
                .build();

        TableViewModel viewModel = new TableViewModel(formStore, model);

        IncidentTableView tableView = new IncidentTableView(formStore, viewModel);

        panel.setWidget(tableView);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public String mayStop() {
        return null;
    }
}
