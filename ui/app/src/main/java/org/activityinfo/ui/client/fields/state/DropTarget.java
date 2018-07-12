package org.activityinfo.ui.client.fields.state;

import java.util.Objects;

public class DropTarget {
    private String groupId;
    private int index;

    public DropTarget(String groupId, int index) {
        this.groupId = groupId;
        this.index = index;
    }

    public String getGroupId() {
        return groupId;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DropTarget that = (DropTarget) o;
        return index == that.index &&
                Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, index);
    }

    @Override
    public String toString() {
        return "DropTarget{" +
                "groupId='" + groupId + '\'' +
                ", index=" + index +
                '}';
    }
}
