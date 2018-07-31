package org.activityinfo.ui.client.header;

import com.google.gwt.user.client.Window;
import org.activityinfo.model.job.ExportResult;
import org.activityinfo.model.job.JobState;
import org.activityinfo.model.job.JobStatus;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.base.toaster.Toast;
import org.activityinfo.ui.client.base.toaster.Toaster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JobNotifier {

    private final Observable<List<JobStatus>> jobs;
    private Set<String> startedJobs = new HashSet<>();
    private Set<String> finishedJobs = new HashSet<>();

    public JobNotifier(Observable<List<JobStatus>> jobs) {
        this.jobs = jobs;
    }

    public void start() {
        jobs.subscribe(list -> {
            if(list.isLoaded()) {
                update(list.get());
            }
        });
    }

    private void update(List<JobStatus> jobs) {
        for (JobStatus job : jobs) {
            if(job.isPending() && !startedJobs.contains(job.getId())) {
                startedJobs.add(job.getId());
                notifyStart(job);
            }
            if(!job.isPending() && !finishedJobs.contains(job.getId())) {
                finishedJobs.add(job.getId());
                notifyComplete(job);
            }
        }
    }


    private void notifyStart(JobStatus job) {

        Toaster.show(new Toast.Builder()
                .success("Starting export...")
                .autoHide()
                .build());
    }

    private void notifyComplete(JobStatus job) {

        if(job.getState() == JobState.FAILED) {
            Toaster.show(new Toast.Builder()
                    .error("Export failed")
                    .autoHide()
                    .build());
        } else if(job.getResult() instanceof ExportResult) {
            Toaster.show(new Toast.Builder()
                    .success("Export complete")
                    .action("Download", () -> {
                        forceDownload(((ExportResult) job.getResult()));
                    })
                    .build());
        }
    }

    private void forceDownload(ExportResult result) {
        Window.open(result.getDownloadUrl(), "#blank", null);
    }


}
