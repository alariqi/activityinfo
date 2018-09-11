package org.activityinfo.server.job;

import com.google.inject.Inject;
import org.activityinfo.analysis.pivot.PivotTableWriter;
import org.activityinfo.analysis.pivot.viewModel.*;
import org.activityinfo.model.analysis.pivot.*;
import org.activityinfo.model.job.ExportPivotTableJob;
import org.activityinfo.model.job.ExportResult;
import org.activityinfo.observable.Observable;
import org.activityinfo.server.generated.GeneratedResource;
import org.activityinfo.server.generated.StorageProvider;
import org.activityinfo.store.query.shared.FormSource;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportPivotTableExecutor implements JobExecutor<ExportPivotTableJob, ExportResult> {

    private static final String CSV_UTF8_MIME = "text/csv;charset=UTF-8";

    private final StorageProvider storageProvider;
    private final FormSource formSource;

    @Inject
    public ExportPivotTableExecutor(StorageProvider storageProvider, FormSource formSource) {
        this.storageProvider = storageProvider;
        this.formSource = formSource;
    }

    @Override
    public ExportResult execute(ExportPivotTableJob descriptor) throws IOException {
        PivotModel pivotModel = descriptor.getPivotModel();
        PivotViewModel viewModel = new PivotViewModel(Observable.just(pivotModel), formSource);
        AnalysisResult pivotTable = viewModel.getResultTable().waitFor();
        GeneratedResource export = storageProvider.create(CSV_UTF8_MIME, fileName());

        try (PivotTableWriter writer = new PivotTableWriter(new OutputStreamWriter(export.openOutputStream(), "UTF-8"))) {
            writer.write(pivotTable);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ExportResult(export.getDownloadUri());
    }

    private String fileName() {
        String date = new SimpleDateFormat("YYYY-MM-dd_HHmmss").format(new Date());
        return ("ActivityInfo_Export_" + date + ".csv").replace(" ", "_");
    }

}
