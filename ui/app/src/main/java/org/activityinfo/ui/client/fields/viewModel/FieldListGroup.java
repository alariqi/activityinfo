package org.activityinfo.ui.client.fields.viewModel;

import java.util.List;

/**
 * A group of fields, such as from "This Form", or "Referenced Fields", or
 * "Measures", etc.
 */
public class FieldListGroup {
    private final String heading;
    private final List<FieldListItem> items;

    public FieldListGroup(String heading, List<FieldListItem> items) {
        this.heading = heading;
        this.items = items;
    }

    public FieldListGroup(List<FieldListItem> items) {
        this.heading = "";
        this.items = items;
    }

    public String getHeading() {
        return heading;
    }

    public List<FieldListItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean hasHeader() {
        return !heading.isEmpty();
    }
}
