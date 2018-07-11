package org.activityinfo.ui.client.fields.viewModel;

import java.util.Arrays;
import java.util.List;

public class ReportElementListView {
    private List<ReportElementGroup> groups;

    public ReportElementListView(List<ReportElementGroup> groups) {
        this.groups = groups;
    }

    public ReportElementListView(ReportElementGroup... groups) {
        this(Arrays.asList(groups));
    }

    public List<ReportElementGroup> getGroups() {
        return groups;
    }
}
