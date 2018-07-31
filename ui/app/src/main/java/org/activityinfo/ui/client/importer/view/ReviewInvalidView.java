package org.activityinfo.ui.client.importer.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.io.csv.CsvWriter;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.analysis.view.OfflineExporter;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.client.importer.viewModel.ImportedTable;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class ReviewInvalidView {

    public static VTree render(ImportViewModel viewModel, ImportUpdater updater) {
        return ImportView.page(viewModel,
                navigation(viewModel, updater),
                body(viewModel));
    }

    private static VTree navigation(ImportViewModel viewModel, ImportUpdater updater) {
        return H.div(
                ImportView.cancelButton(),
                ImportView.backButton(ImportState.ImportStep.MATCH_COLUMNS, updater),
                ImportView.doneButton());
    }

    private static VTree body(ImportViewModel viewModel) {
        return new ReactiveComponent(viewModel.getImportedTable().transform(table -> {
            return H.div("importer",
                    H.div("importer__main",
                            ImportView.heading(viewModel, I18N.CONSTANTS.reviewInvalidRecords(),
                                    validCountHeader(table),
                                    invalidCountHeader(table),
                                    downloadInvalidButton(table)),
                            H.div("importer__body importer__verify",
                                    InvalidDataTable.render(table))));
        }));
    }


    private static VTree validCountHeader(ImportedTable table) {
        return H.p(Props.withClass("importer__valid-count"),
                new VText(I18N.MESSAGES.validRecordCount(table.getValidRecordCount())));
    }

    private static VTree invalidCountHeader(ImportedTable table) {
        return H.p(I18N.MESSAGES.invalidRecordCount(table.getInvalidRecordCount()));
    }

    private static VTree downloadInvalidButton(ImportedTable table) {
        return H.div(Buttons
                .button(I18N.CONSTANTS.downloadInvalidRecordsAsCSV())
                .primary()
                .icon(Icon.BUBBLE_EXPORT)
                .onSelect(event -> exportInvalid(table))
                .build());
    }

    private static void exportInvalid(ImportedTable table) {
        OfflineExporter.export("invalid.csv", table.invalidCsv(), CsvWriter.MIME_TYPE);
    }

}
