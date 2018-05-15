package org.activityinfo.dev.client;

import org.activityinfo.model.analysis.Analysis;
import org.activityinfo.model.analysis.AnalysisUpdate;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.form.CatalogEntry;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.form.RecordHistory;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.job.JobDescriptor;
import org.activityinfo.model.job.JobResult;
import org.activityinfo.model.job.JobStatus;
import org.activityinfo.model.query.ColumnSet;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.RecordTransaction;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.promise.Promise;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.store.offline.FormOfflineStatus;

import java.util.Collections;
import java.util.List;

public class DevFormStore implements FormStore {

    @Override
    public Observable<Maybe<UserDatabaseMeta>> getDatabase(ResourceId databaseId) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<List<UserDatabaseMeta>> getDatabases() {
        return Observable.just(Collections.emptyList());
    }

    @Override
    public Observable<FormMetadata> getFormMetadata(ResourceId formId) {
        return Observable.just(FormMetadata.notFound(formId));
    }

    @Override
    public Observable<FormTree> getFormTree(ResourceId formId) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<Maybe<RecordTree>> getRecordTree(RecordRef rootRecordId) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<Maybe<FormRecord>> getRecord(RecordRef recordRef) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<ColumnSet> query(QueryModel queryModel) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<Maybe<Analysis>> getAnalysis(String id) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Promise<Void> deleteForm(ResourceId formId) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<List<CatalogEntry>> getCatalogRoots() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<List<CatalogEntry>> getCatalogChildren(ResourceId parentId) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<List<FormRecord>> getSubRecords(ResourceId formId, RecordRef parent) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void setFormOffline(ResourceId formId, boolean offline) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<FormOfflineStatus> getOfflineStatus(ResourceId formId) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Observable<RecordHistory> getFormRecordHistory(RecordRef ref) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Promise<Void> updateRecords(RecordTransaction tx) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Promise<Void> updateAnalysis(AnalysisUpdate update) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <T extends JobDescriptor<R>, R extends JobResult> Observable<JobStatus<T, R>> startJob(T job) {
        throw new UnsupportedOperationException("TODO");
    }
}
