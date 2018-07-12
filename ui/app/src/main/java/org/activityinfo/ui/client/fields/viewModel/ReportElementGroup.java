package org.activityinfo.ui.client.fields.viewModel;

import java.util.List;

public class ReportElementGroup {
    private String id;
    private String heading;
    private List<ReportElement> items;
    private final int dropPlaceholderIndex;

    public ReportElementGroup(String id, String heading, List<ReportElement> items, int dropPlaceholderIndex) {
        this.id = id;
        this.heading = heading;
        this.items = items;
        this.dropPlaceholderIndex = dropPlaceholderIndex;
    }


    public String getId() {
        return id;
    }

    public boolean isDropTarget() {
        return dropPlaceholderIndex >= 0;
    }

    public int getDropInsertIndex() {
        return dropPlaceholderIndex;
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
