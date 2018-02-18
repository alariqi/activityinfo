package chdc.frontend.client.entry;

import chdc.frontend.client.i18n.ChdcLabels;
import chdc.frontend.client.table.TablePlace;
import chdc.frontend.client.theme.*;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.ui.client.input.view.FormInputView;
import org.activityinfo.ui.client.store.FormStore;

import java.util.logging.Logger;

public class DataEntryWidget implements IsWidget, HasSidebar {


    private static final Logger LOGGER = Logger.getLogger(DataEntryWidget.class.getName());

    private final BorderLayoutContainer container;
    private final FormStore formStore;

    public DataEntryWidget(FormStore formStore, RecordRef recordRef) {
        this.formStore = formStore;

        FormInputView inputView = new FormInputView(formStore, recordRef, new IncidentFormAppearance());


        // Action bar
        IconButton saveButton = new IconButton(Icon.SAVE, I18N.CONSTANTS.save(), IconStyle.SMALL_DIMMED);
        saveButton.addSelectHandler(event -> inputView.save(this::onSaved));

        LinkButton addAnotherLink = new LinkButton(ChdcLabels.LABELS.addAnotherIncident(),
                new DataEntryPlace(recordRef.getFormId()).toUri());
        LinkButton closeLink = new LinkButton(I18N.CONSTANTS.close(),
                new TablePlace(recordRef.getFormId()).toUri());


        FlowLayoutContainer secondary = new FlowLayoutContainer();
        secondary.setStyleName(ChdcTheme.STYLES.actionbarSecondary());
        secondary.add(saveButton);

        FlowLayoutContainer primary = new FlowLayoutContainer();
        primary.setStyleName(ChdcTheme.STYLES.actionbarPrimary());
        primary.add(addAnotherLink);
        primary.add(closeLink);

        ActionBar actionBar = new ActionBar();
        actionBar.addShortcut(secondary);
        actionBar.addShortcut(primary);

        // Side bar

        IncidentSidebar sideBar = new IncidentSidebar();

        container = new BorderLayoutContainer();
        container.setWestWidget(sideBar, new BorderLayoutContainer.BorderLayoutData(340));
        container.setCenterWidget(inputView);
        container.setSouthWidget(actionBar, new BorderLayoutContainer.BorderLayoutData(60));
    }

    private void onSaved(CloseEvent closeEvent) {

    }

    @Override
    public Widget asWidget() {
        return container;
    }

}
