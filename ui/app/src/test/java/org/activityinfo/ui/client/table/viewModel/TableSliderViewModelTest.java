package org.activityinfo.ui.client.table.viewModel;

import net.lightoze.gwt.i18n.server.LocaleProxy;
import org.activityinfo.observable.Connection;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.promise.Maybe;
import org.activityinfo.store.testing.ReferralSubForm;
import org.activityinfo.ui.client.store.TestSetup;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.state.SliderState;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TableSliderViewModelTest {


    private TestSetup setup = new TestSetup();

    @Before
    public void setup() {
        LocaleProxy.initialize();
    }

    @Test
    public void subform()  {

        ReferralSubForm referralSubForm = setup.getCatalog().getReferralSubForm();
        String parentRecordId = referralSubForm.getRecords().get(0).getParentRecordId().asString();

        TablePlace tablePlace = new TablePlace(referralSubForm.getFormId(), parentRecordId);
        StatefulValue state = new StatefulValue(new SliderState(tablePlace));

        Observable<Maybe<TableSliderViewModel>> viewModel = TableSliderViewModel.compute(setup.getFormStore(), state);
        Connection<Maybe<TableSliderViewModel>> view = setup.connect(viewModel);

        assertThat(view.assertLoaded().getState(), equalTo(Maybe.State.VISIBLE));

        TableSliderViewModel sliderViewModel = view.assertLoaded().get();
        assertThat(sliderViewModel.getSlideCount(), equalTo(2));
        assertThat(sliderViewModel.getSliderPosition().get().getSlideIndex(), equalTo(1));

        TableViewModel subFormTableViewModel = sliderViewModel.getTables().get(1);
        assertThat(subFormTableViewModel.getFormId(), equalTo(referralSubForm.getFormId()));
        assertThat(subFormTableViewModel.getParentRecordId().get(), equalTo(parentRecordId));


    }

}