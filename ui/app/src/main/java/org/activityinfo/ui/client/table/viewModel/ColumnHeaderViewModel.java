package org.activityinfo.ui.client.table.viewModel;

import org.activityinfo.analysis.table.Sorting;

import java.util.Objects;

public class ColumnHeaderViewModel {
    private Sorting sortOrder;
    private boolean filtered;

    public ColumnHeaderViewModel(Sorting sortOrder, boolean filtered) {
        this.sortOrder = sortOrder;
        this.filtered = filtered;
    }

    public Sorting getSortOrder() {
        return sortOrder;
    }

    public boolean isFiltered() {
        return filtered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnHeaderViewModel that = (ColumnHeaderViewModel) o;
        return filtered == that.filtered &&
                Objects.equals(sortOrder, that.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortOrder, filtered);
    }
}
