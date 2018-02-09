package chdc.frontend.client.entry;

import chdc.frontend.client.i18n.ChdcLabels;
import chdc.frontend.client.table.TablePlace;
import chdc.frontend.client.theme.HasSidebar;
import chdc.frontend.client.theme.Icon;
import chdc.frontend.client.theme.IconButton;
import chdc.frontend.client.theme.LinkButton;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
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

        FormInputView inputView = new FormInputView(formStore, recordRef);

        ToolBar toolBar = new ToolBar();

        // Action bar
        IconButton saveButton = new IconButton(Icon.SAVE, I18N.CONSTANTS.save());
        saveButton.addSelectHandler(event -> inputView.save(this::onSaved));

        LinkButton addAnotherLink = new LinkButton(ChdcLabels.LABELS.addAnotherIncident(),
                new DataEntryPlace(recordRef.getFormId()).toUri());
        LinkButton closeLink = new LinkButton(I18N.CONSTANTS.close(),
                new TablePlace(recordRef.getFormId()).toUri());

        toolBar.add(saveButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(addAnotherLink);
        toolBar.add(closeLink);

        DataEntrySidebar sideBar = new DataEntrySidebar();

        container = new BorderLayoutContainer();
        container.setWestWidget(sideBar, new BorderLayoutContainer.BorderLayoutData(340));
        container.setCenterWidget(inputView);
        container.setSouthWidget(toolBar, new BorderLayoutContainer.BorderLayoutData(60));
    }

    private void onSaved(CloseEvent closeEvent) {

    }

    @Override
    public Widget asWidget() {
        return container;
    }

}
