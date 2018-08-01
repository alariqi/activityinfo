package org.activityinfo.ui.client.importer;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.model.resource.RecordTransaction;
import org.activityinfo.model.resource.RecordTransactionBuilder;
import org.activityinfo.model.resource.RecordUpdate;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.importer.state.CommitProgress;
import org.activityinfo.ui.client.importer.state.CommitStatus;
import org.activityinfo.ui.client.importer.viewModel.ImportedTable;
import org.activityinfo.ui.client.store.FormStore;

import java.util.Iterator;

public class RecordImporter {

    private static final int BATCH_SIZE = 10;

    private static final int RETRY_DELAY_MS = 1500;

    private final FormStore formStore;
    private final ImportedTable table;
    private final Iterator<RecordUpdate> recordIt;
    private final StatefulValue<CommitProgress> progress;

    private Runnable onComplete;
    private RecordTransaction pendingTransaction;

    private int recordsUpdated = 0;

    private boolean cancelled = false;


    public RecordImporter(FormStore formStore, ImportedTable table) {
        this.formStore = formStore;
        this.table = table;
        this.progress = new StatefulValue<>(new CommitProgress(table.getValidRecordCount()));
        recordIt = this.table.getRecords();
    }

    public Observable<CommitProgress> getProgress() {
        return progress;
    }

    public void start(Runnable onComplete) {
        this.onComplete = onComplete;
        queueNextBatch();
    }

    private void queueNextBatch() {

        if(cancelled) {
            return;
        }

        if(!recordIt.hasNext()) {
            onComplete.run();
            return;
        }

        RecordTransactionBuilder batch = new RecordTransactionBuilder();
        int recordsAdded = 0;
        while(recordIt.hasNext() && recordsAdded < BATCH_SIZE) {
            batch.add(recordIt.next());
            recordsAdded++;
        }

        pendingTransaction = batch.build();

        submitPending();
    }

    private void submitPending() {
        formStore.updateRecords(pendingTransaction).then(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                progress.update(s -> s.withStatus(CommitStatus.CONNECTION_PROBLEM));
                scheduleRetry();
            }

            @Override
            public void onSuccess(Void aVoid) {
                recordsUpdated += pendingTransaction.getChangeCount();
                progress.update(s -> s.withRecordsCommitted(recordsUpdated));
                queueNextBatch();
            }

        });
    }

    private void scheduleRetry() {
        Scheduler.get().scheduleFixedPeriod(() -> {
            progress.update(s -> s.withStatus(CommitStatus.RETRYING));
            submitPending();
            return false;
        }, RETRY_DELAY_MS);
    }


    public int getRecordsCommitted() {
        return progress.get().getRecordsCommitted();
    }

    public void cancel() {
        cancelled = true;
    }
}
