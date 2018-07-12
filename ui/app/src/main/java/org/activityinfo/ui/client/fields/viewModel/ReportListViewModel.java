package org.activityinfo.ui.client.fields.viewModel;

import java.util.Arrays;
import java.util.List;

public class ReportListViewModel {
    private List<ReportElementGroup> groups;

    public ReportListViewModel(List<ReportElementGroup> groups) {
        this.groups = groups;
    }

    public ReportListViewModel(ReportElementGroup... groups) {
        this(Arrays.asList(groups));
    }

    public List<ReportElementGroup> getGroups() {
        return groups;
    }
}
