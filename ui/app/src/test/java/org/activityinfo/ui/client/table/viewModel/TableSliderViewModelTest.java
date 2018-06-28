package org.activityinfo.ui.client.table.viewModel;

import org.activityinfo.observable.Connection;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.store.testing.IncidentForm;
import org.activityinfo.store.testing.ReferralSubForm;
import org.activityinfo.ui.client.store.TestSetup;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.model.TableModelStore;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TableSliderViewModelTest {

    private TestSetup setup = new TestSetup();

    @Test
    public void root() {

        IncidentForm incidentForm = setup.getCatalog().getIncidentForm();

        TablePlace tablePlace = new TablePlace(incidentForm.getFormId());

        Observable<Maybe<TableSliderViewModel>> viewModel = TableSliderViewModel.compute(
                setup.getFormStore(),
                TableModelStore.STORE,
                tablePlace);

        Connection<Maybe<TableSliderViewModel>> view = setup.connect(viewModel);

        assertThat(view.assertLoaded().get().getPageTitle(), equalTo(incidentForm.getFormClass().getLabel()));
        assertThat(view.assertLoaded().get().getTables(), Matchers.hasSize(2));

        TableViewModel table = view.assertLoaded().get().getTables().get(0);
        assertThat(table.getDepth(), equalTo(0));
        assertThat(table.isVisible(), equalTo(true));

    }

    @Test
    public void firstSubForm() {
        IncidentForm incidentForm = setup.getCatalog().getIncidentForm();
        ReferralSubForm subForm = setup.getCatalog().getReferralSubForm();

        TablePlace tablePlace = new TablePlace(incidentForm.getFormId()).subform(subForm.getFormId(), incidentForm.getRecordRef(11));

        Observable<Maybe<TableSliderViewModel>> viewModel = TableSliderViewModel.compute(
                setup.getFormStore(),
                TableModelStore.STORE,
                tablePlace);

        Connection<Maybe<TableSliderViewModel>> view = setup.connect(viewModel);

        assertThat(view.assertLoaded().get().getPageTitle(), equalTo(subForm.getFormClass().getLabel()));
        assertThat(view.assertLoaded().get().getTables(), Matchers.hasSize(2));
        assertThat(view.assertLoaded().get().getSlideIndex(), equalTo(1));

        TableViewModel table = view.assertLoaded().get().getTables().get(0);
        assertThat(table.getDepth(), equalTo(0));
        assertThat(table.isVisible(), equalTo(true));

        TableViewModel subTable = view.assertLoaded().get().getTables().get(1);
        assertThat(subTable.getDepth(), equalTo(1));
        assertThat(table.isVisible(), equalTo(true));
    }

}