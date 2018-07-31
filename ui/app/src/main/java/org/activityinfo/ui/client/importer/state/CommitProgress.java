package org.activityinfo.ui.client.importer.state;

public class CommitProgress {
    private CommitStatus status;
    private int totalRecords;
    private int recordsCommitted;

    public CommitProgress(int totalRecords) {
        this.status = CommitStatus.RUNNING;
        this.totalRecords = totalRecords;
        this.recordsCommitted = 0;
    }

    private CommitProgress(CommitProgress from) {
        this.status = from.status;
        this.totalRecords = from.totalRecords;
        this.recordsCommitted = from.recordsCommitted;
    }

    public CommitStatus getStatus() {
        return status;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getRecordsCommitted() {
        return recordsCommitted;
    }

    public CommitProgress withStatus(CommitStatus newStatus) {
        CommitProgress copy = new CommitProgress(this);
        copy.status = newStatus;
        return copy;
    }

    public CommitProgress withRecordsCommitted(int records) {
        CommitProgress copy = new CommitProgress(this);
        copy.recordsCommitted = records;
        return copy;
    }
}
