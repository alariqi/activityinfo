package org.activityinfo.ui.client.fields.viewModel;

import java.util.List;

public class ReportElementGroup {
    private String heading;
    private List<ReportElement> items;

    public ReportElementGroup(String heading, List<ReportElement> items) {
        this.heading = heading;
        this.items = items;
    }

    public String getHeading() {
        return heading;
    }

    public List<ReportElement> getItems() {
        return items;
    }

    public boolean hasHeader() {
        return !heading.isEmpty();
    }
}
