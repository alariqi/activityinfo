package org.activityinfo.ui.client.base.datatable;

import java.util.Objects;

public class RowRange {
    private int startIndex;
    private int rowCount;

    public RowRange() {
    }

    public RowRange(int startIndex, int rowCount) {
        assert rowCount > 0;
        this.startIndex = startIndex;
        this.rowCount = rowCount;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return startIndex + rowCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RowRange rowRange = (RowRange) o;
        return startIndex == rowRange.startIndex &&
                rowCount == rowRange.rowCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIndex, rowCount);
    }

    public static RowRange fromScroll(double scrollTop, int clientHeight, double rowHeight) {
        int startIndex = (int) (scrollTop / rowHeight);
        int rowCount = (int)(clientHeight / rowHeight);
        return new RowRange(startIndex, rowCount);
    }

    public boolean contains(RowRange otherRange) {
        return this.startIndex <= otherRange.startIndex &&
                otherRange.getEndIndex() <= this.getEndIndex();

    }

    public RowRange withRowCount(int count) {
        if(this.rowCount < count) {
            int difference = count - this.rowCount;
            int bufferedStartIndex = this.startIndex - (difference / 2);
            if(bufferedStartIndex < 0) {
                bufferedStartIndex = 0;
            }
            return new RowRange(bufferedStartIndex, count);
        } else {
            return this;
        }
    }
}
