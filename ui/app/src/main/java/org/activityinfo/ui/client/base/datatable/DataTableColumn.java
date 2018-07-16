package org.activityinfo.ui.client.base.datatable;

import org.activityinfo.analysis.table.Sorting;
import org.activityinfo.model.annotation.AutoBuilder;

@AutoBuilder
public class DataTableColumn {
    int width = 150;
    String id = "";
    String surtitle = "";
    String heading;
    Sorting sorting = Sorting.NONE;
    boolean filterActive = false;
    String headingClass = "";
    boolean columnSelected = false;

    protected DataTableColumn() {
    }

    public String getId() {
        return id;
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
        return !surtitle.isEmpty();
    }

    public String getHeadingClass() {
        return headingClass;
    }

    public boolean isColumnSelected() {
        return columnSelected;
    }
}
