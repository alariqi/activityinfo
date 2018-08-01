package org.activityinfo.ui.client.importer;

import com.google.gwt.user.client.History;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.AppFrame;
import org.activityinfo.ui.client.Page;
import org.activityinfo.ui.client.Place;
import org.activityinfo.ui.client.base.modal.AppModal;
import org.activityinfo.ui.client.base.toaster.Toast;
import org.activityinfo.ui.client.base.toaster.Toaster;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.view.CommittingView;
import org.activityinfo.ui.client.importer.view.ImportView;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.client.importer.viewModel.ImportedTable;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.function.Function;

public class ImportPage extends Page implements ImportUpdater {

    private final FormStore formStore;

    private StatefulValue<ImportState> state;
    private Observable<Maybe<ImportViewModel>> viewModel;

    private boolean importComplete = false;
    private AppModal modal;
    private RecordImporter importer;

    public ImportPage(FormStore formStore, ImportPlace place) {
        this.formStore = formStore;
        this.state = new StatefulValue<>(new ImportState(place.getFormId()));
        this.viewModel = ImportViewModel.compute(formStore, state);
    }

    @Override
    public boolean tryNavigate(Place place) {
        return false;
    }

    @Override
    public String mayStop() {
        if(!importComplete && !state.get().isEmpty()) {
            return I18N.CONSTANTS.unsavedChangesWarning();
        }
        return null;
    }


    @Override
    public VTree render() {
        return AppFrame.render(formStore, ImportView.render(viewModel, this));
    }

    @Override
    public void update(Function<ImportState, ImportState> function) {
        state.update(function);
    }

    @Override
    public void startImport(ImportedTable table) {
        importer = new RecordImporter(formStore, table);

        modal = new AppModal(CommittingView.render(importer.getProgress()));
        modal.show();

        importer.start(() -> {
            modal.hide();
            onImportComplete(table, importer);
        });
    }

    private void onImportComplete(ImportedTable table, RecordImporter importer) {

        importComplete = true;

        Toaster.show(new Toast.Builder()
            .success(I18N.MESSAGES.recordsImported(importer.getRecordsCommitted()))
            .autoHide(7500)
            .build());

        // Navigate to the table view
        History.replaceItem(new TablePlace(table.getFormId()).toString());
    }

    @Override
    public void stop() {
        if(importer != null) {
            importer.cancel();
        }
        if(modal != null) {
            modal.hide();
        }
    }
}
