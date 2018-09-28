package org.activityinfo.ui.client.reports;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.AppFrame;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.Page;
import org.activityinfo.ui.client.base.EmptyStateBuilder;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.base.listtable.ListItem;
import org.activityinfo.ui.client.base.listtable.ListTable;
import org.activityinfo.ui.client.base.toolbar.ToolbarBuilder;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.reports.pivot.PivotPlace;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.Collections;
import java.util.List;

public class ReportListPage extends Page {


    private final FormStore store;

    public ReportListPage(FormStore store) {
        this.store = store;
    }

    @Override
    public VTree render() {

        Observable<List<ListItem>> reports = Observable.just(Collections.emptyList());

        VTree addTable = Buttons.button(I18N.CONSTANTS.addTable())
                .icon(Icon.BUBBLE_ADD)
                .primary()
                .onSelect(this::addPivotTable)
                .build();

        VTree addMap = Buttons.button(I18N.CONSTANTS.addMap())
                .icon(Icon.BUBBLE_ADD)
                .primary()
                .onSelect(this::addPivotTable)
                .build();

        VTree toolBar = new ToolbarBuilder()
                .action(addTable)
                .action(addMap)
                .build();

        VTree reportList = new ListTable(reports)
                .emptyState(new EmptyStateBuilder()
                    .setExplanationText(I18N.CONSTANTS.emptyReports())
                    .build())
                .render();

        VTree page = new PageBuilder()
                .padded()
                .heading(I18N.CONSTANTS.reports())
                .body(H.div(toolBar, reportList))
                .build();

        return AppFrame.render(store, page);
    }

    private void addPivotTable(Event event) {
        String reportId = ResourceId.generateCuid();
        PivotPlace place = new PivotPlace(reportId);

        History.newItem(place.toString());
    }
}
