package org.activityinfo.ui.client.reports.formSelection.viewModel;

import org.activityinfo.model.resource.ResourceId;

import java.util.Objects;

public class FormSelectionItem implements Comparable<FormSelectionItem> {



    public enum Selection {
        ALL,
        NONE,
        SOME
    }

    public enum ItemType {
        ROOT,
        DATABASE,
        FOLDER,
        FORM,
        SUBFORM

    }

    private ResourceId id;
    private final ItemType type;
    private Selection selected;
    private String label;

    public FormSelectionItem(ResourceId id, ItemType type, String label, Selection selected) {
        this.id = id;
        this.type = type;
        this.label = label;
        this.selected = selected;
    }


    public ResourceId getId() {
        return id;
    }

    public Selection getSelected() {
        return selected;
    }

    public String getLabel() {
        return label;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormSelectionItem that = (FormSelectionItem) o;
        return Objects.equals(id, that.id) &&
                selected == that.selected &&
                Objects.equals(label, that.label);
    }

    @Override
    public int compareTo(FormSelectionItem o) {
        if(this.getType() == o.getType()) {
            return this.getLabel().compareToIgnoreCase(o.getLabel());
        }  else {
            return this.getType().compareTo(o.getType());
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, selected, label);
    }

    @Override
    public String toString() {
        return "FormSelectionItem{" +
                "id=" + id +
                ", selected=" + selected +
                ", label='" + label + '\'' +
                '}';
    }
}
