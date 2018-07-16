package org.activityinfo.ui.client.importer;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.AppFrame;
import org.activityinfo.ui.client.Page;
import org.activityinfo.ui.client.Place;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.view.ImportView;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.function.Function;

public class ImportPage extends Page implements ImportUpdater {

    private final FormStore formStore;

    private StatefulValue<ImportState> state;
    private Observable<Maybe<ImportViewModel>> viewModel;

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
        if(!state.get().isEmpty()) {
            return I18N.CONSTANTS.unsavedChangesWarning();
        }
        return null;
    }

    @Override
    public VTree render() {
        return AppFrame.render(formStore, ImportView.render(formStore, viewModel, this));
    }

    @Override
    public void update(Function<ImportState, ImportState> function) {
        state.update(function);
    }
}
