package org.activityinfo.ui.client.base.datatable;

import org.activityinfo.analysis.table.Sorting;
import org.activityinfo.model.annotation.AutoBuilder;

@AutoBuilder
public class DataTableColumn {
    int width = 150;
    String surtitle;
    String heading;
    Sorting sorting;
    boolean filterActive;

    protected DataTableColumn() {
    }

    public int getWidth() {
        return width;
    }

    public String getSurtitle() {
        return surtitle;
    }

    public String getHeading() {
        return heading;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public boolean hasSurtitle() {
        return surtitle != null;
    }
}
