package org.activityinfo.ui.client.analysis.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.analysis.model.Axis;
import org.activityinfo.ui.client.analysis.viewModel.AnalysisViewModel;
import org.activityinfo.ui.client.analysis.viewModel.EffectiveDimension;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.List;

public class DimensionView {

    public static VNode render(AnalysisViewModel viewModel) {
        return H.div("dimensions",
                H.h3(I18N.CONSTANTS.measures()),
                measureList(),
                H.h3(I18N.CONSTANTS.rows()),
                dimensionList(viewModel.getDimensionListItems(), Axis.ROW),
                H.h3(I18N.CONSTANTS.columns()),
                dimensionList(viewModel.getDimensionListItems(), Axis.COLUMN));
    }

    private static VTree dimensionList(Observable<List<EffectiveDimension>> dimensions, Axis axis) {
        return H.div("dimensions__empty", new VText("Drag field here."));
    }

    private static VTree measureList() {
        return H.div("dimensions__empty", new VText("Drag field here."));
    }

}
