package chdc.frontend.client.table;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.store.FormStore;

public class TableActivity implements Activity {


    private final FormStore formStore;
    private final TablePlace place;
    private TableFrame tableFrame;

    public TableActivity(FormStore formStore, TablePlace place) {
        this.formStore = formStore;
        this.place = place;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        tableFrame = new TableFrame(formStore);
        panel.setWidget(tableFrame);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public String mayStop() {
        if(tableFrame != null && tableFrame.hasUnsavedChanges()) {
            return I18N.CONSTANTS.unsavedChangesWarning();
        }
        return null;
    }
}
