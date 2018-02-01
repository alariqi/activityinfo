package chdc.frontend.client.entry;

import chdc.frontend.client.i18n.ChdcLabels;
import chdc.frontend.client.table.TablePlace;
import chdc.frontend.client.theme.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.json.Json;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.RecordTransaction;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.input.view.InputHandler;
import org.activityinfo.ui.client.input.viewModel.ActivePeriodMemory;
import org.activityinfo.ui.client.input.viewModel.FormInputViewModel;
import org.activityinfo.ui.client.input.viewModel.FormInputViewModelBuilder;
import org.activityinfo.ui.client.input.viewModel.LiveActivePeriodMemory;
import org.activityinfo.ui.client.store.FormStore;

import java.util.logging.Logger;

public class DataEntryWidget implements IsWidget, HasSidebar {

    private final ActivePeriodMemory memory = new LiveActivePeriodMemory();

    private static final Logger LOGGER = Logger.getLogger(DataEntryWidget.class.getName());

    private final MainContainer container;
    private final FormStore formStore;
    private final StatefulValue<FormInputModel> inputModel;
    private final Observable<FormInputViewModel> viewModel;

    public DataEntryWidget(FormStore formStore, RecordRef recordRef) {
        this.formStore = formStore;

        // Define the current state of data input
        inputModel = new StatefulValue<>(new FormInputModel(recordRef));

        // Compute the view model
        Observable<FormTree> formTree = formStore.getFormTree(recordRef.getFormId());
        Observable<FormInputViewModelBuilder> builder = formTree.transform(tree -> new FormInputViewModelBuilder(formStore, tree, memory));
        viewModel = Observable.transform(builder, inputModel, (b, m) -> b.build(m));

        // Main content
        // Form Panel
        IncidentFormPanel formPanel = new IncidentFormPanel(formStore, recordRef, new Handler(), viewModel);

        FlowLayoutContainer mainContent = new FlowLayoutContainer();
        mainContent.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
        mainContent.setStyleName("maincontent");
        mainContent.add(formPanel);

        // Action bar
        IconButton saveButton = new IconButton(Icon.SAVE, I18N.CONSTANTS.save());
        saveButton.addSelectHandler(this::save);

        LinkButton addAnotherLink = new LinkButton(ChdcLabels.LABELS.addAnotherIncident(),
                new DataEntryPlace(recordRef.getFormId()).toUri());
        LinkButton closeLink = new LinkButton(I18N.CONSTANTS.close(),
                new TablePlace(recordRef.getFormId()).toUri());


        FlowLayoutContainer secondary = new FlowLayoutContainer();
        secondary.setStyleName("actionbar__secondary");
        secondary.add(saveButton);

        FlowLayoutContainer primary = new FlowLayoutContainer();
        primary.setStyleName("actionbar__primary");
        primary.add(addAnotherLink);
        primary.add(closeLink);

        ActionBar actionBar = new ActionBar();
        actionBar.addShortcut(secondary);
        actionBar.addShortcut(primary);

        // Side bar

        DataEntrySidebar sideBar = new DataEntrySidebar();

        this.container = new MainContainer();
        this.container.add(mainContent);
        this.container.add(actionBar);
        this.container.add(sideBar);
    }

    private void save(SelectEvent event) {
        if (!viewModel.isLoaded()) {
            return;
        }

        if(!viewModel.get().isValid()) {
            Window.alert(I18N.CONSTANTS.pleaseFillInAllRequiredFields());
            return;
        }

        RecordTransaction tx = viewModel.get().buildTransaction();

        LOGGER.info("Submitting transaction: " + Json.stringify(tx));

        formStore.updateRecords(tx).then(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(I18N.CONSTANTS.errorOnServer());
            }

            @Override
            public void onSuccess(Void result) {
                Window.alert(I18N.CONSTANTS.saved());
            }
        });

    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private class Handler implements InputHandler {

        @Override
        public void updateModel(RecordRef record, ResourceId fieldId, FieldInput value) {
            FormInputModel updatedModel = inputModel.get().update(record, fieldId, value);
            inputModel.updateIfNotSame(updatedModel);
        }

        @Override
        public void addSubRecord(RecordRef subRecordRef) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteSubRecord(RecordRef recordRef) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void changeActiveSubRecord(ResourceId fieldId, RecordRef newActiveRef) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void updateSubModel(FormInputModel update) {
            throw new UnsupportedOperationException();
        }
    }
}
