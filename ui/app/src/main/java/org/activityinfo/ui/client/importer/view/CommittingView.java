package org.activityinfo.ui.client.importer.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.alert.Alerts;
import org.activityinfo.ui.client.base.progressbar.ProgressBar;
import org.activityinfo.ui.client.importer.state.CommitProgress;
import org.activityinfo.ui.client.importer.state.CommitStatus;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class CommittingView {

    public static VTree render(Observable<CommitProgress> state) {
        return new ReactiveComponent(state.transform(CommittingView::status));
    }

    private static VTree status(CommitProgress state) {
        return H.div("importer__commit",
                doNotCloseWarning(),
                H.div("importer__commit__progress",
                    statusHeader(state.getStatus()),
                    ProgressBar.bar(state.getRecordsCommitted(), state.getTotalRecords(),
                            I18N.MESSAGES.recordsProcessed(state.getRecordsCommitted()))));
    }


    private static VTree doNotCloseWarning() {
        return Alerts.warning(I18N.CONSTANTS.doNotClose());
    }

    private static VTree statusHeader(CommitStatus status) {
        switch (status) {
            default:
                return H.h3(I18N.CONSTANTS.importInProgress());
            case CONNECTION_PROBLEM:
                return H.h3(I18N.CONSTANTS.connectionProblem());
            case RETRYING:
                return H.h3(I18N.CONSTANTS.retrying());
        }
    }
}
