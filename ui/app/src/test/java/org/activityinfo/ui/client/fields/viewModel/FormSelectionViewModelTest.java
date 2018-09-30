package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.store.testing.IraqDatabase;
import org.activityinfo.ui.client.reports.formSelection.state.FormPath;
import org.activityinfo.ui.client.reports.formSelection.viewModel.FormSelectionViewModel;
import org.activityinfo.ui.client.store.TestSetup;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class FormSelectionViewModelTest {

    private TestSetup setup = new TestSetup();

    @Test
    public void selectDatabase() {

        setup.describeDatabase(IraqDatabase.database());

        FormPath model = new FormPath()
                .navigateTo(0, FormPath.DATABASE_ROOT_ID);

        Observable<Set<ResourceId>> selection = Observable.just(Collections.emptySet());

        Observable<FormSelectionViewModel> viewModelObs = FormSelectionViewModel.compute(setup.getFormStore(), selection, Observable.just(model));
        FormSelectionViewModel viewModel = setup.connect(viewModelObs).assertLoaded();

        assertThat(viewModel.getColumns(), hasSize(2));

        assertThat(viewModel.getColumns().get(0).getItems().get(0), hasProperty("label", equalTo("Databases")));

    }


}