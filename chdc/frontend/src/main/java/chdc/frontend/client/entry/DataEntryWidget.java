package chdc.frontend.client.entry;

import chdc.frontend.client.i18n.ChdcLabels;
import chdc.frontend.client.table.TablePlace;
import chdc.frontend.client.theme.*;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.input.view.InputHandler;
import org.activityinfo.ui.client.input.viewModel.*;
import org.activityinfo.ui.client.store.FormStore;

public class DataEntryWidget implements IsWidget, HasSidebar {

    private final ActivePeriodMemory memory = new LiveActivePeriodMemory();

    private final MainContainer container;
    private final StatefulValue<FormInputModel> inputModel;

    public DataEntryWidget(FormStore formStore, RecordRef recordRef) {

        // Define the current state of data input
        inputModel = new StatefulValue<>(new FormInputModel(recordRef));

        // Compute the view model
        Observable<FormTree> formTree = formStore.getFormTree(recordRef.getFormId());
        Observable<FormInputViewModelBuilder> builder = formTree.transform(tree -> new FormInputViewModelBuilder(formStore, tree, memory));
        Observable<FormInputViewModel> viewModel = Observable.transform(builder, inputModel, (b, m) -> b.build(m));

        // Main content
        // Form Panel
        IncidentFormPanel formPanel = new IncidentFormPanel(formStore, recordRef, new Handler(), viewModel);

        FlowLayoutContainer mainContent = new FlowLayoutContainer();
        mainContent.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
        mainContent.setStyleName("maincontent");
        mainContent.add(formPanel);

        // Action bar
        IconButton saveButton = new IconButton(Icon.SAVE, I18N.CONSTANTS.save());
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