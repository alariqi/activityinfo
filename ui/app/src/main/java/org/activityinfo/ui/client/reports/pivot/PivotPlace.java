package org.activityinfo.ui.client.reports.pivot;

import org.activityinfo.ui.client.Place;

public class PivotPlace extends Place {
    private String id;

    public PivotPlace(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "report/" + id;
    }
}
