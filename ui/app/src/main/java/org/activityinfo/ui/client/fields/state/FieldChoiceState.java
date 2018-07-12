package org.activityinfo.ui.client.fields.state;

import java.util.Optional;

public class FieldChoiceState {
    private boolean expanded;
    private DesignMode mode;
    private Optional<String> draggingFieldId;
    private Optional<String> draggingColumnId;
    private Optional<DropTarget> dropTarget;


    public FieldChoiceState(boolean expanded) {
        this.expanded = expanded;
        this.mode = DesignMode.NORMAL;
        this.draggingFieldId = Optional.empty();
        this.draggingColumnId = Optional.empty();
        this.dropTarget = Optional.empty();
    }

    private FieldChoiceState(FieldChoiceState from) {
        this.expanded = from.expanded;
        this.draggingFieldId = from.draggingFieldId;
        this.draggingColumnId = from.draggingColumnId;
        this.dropTarget = from.dropTarget;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public DesignMode getMode() {
        return mode;
    }

    public Optional<String> getDraggingFieldId() {
        return draggingFieldId;
    }

    public Optional<String> getDraggingColumnId() {
        return draggingColumnId;
    }

    public FieldChoiceState withMode(DesignMode mode) {
        FieldChoiceState updated = new FieldChoiceState(this);
        updated.mode = mode;
        return this;
    }

    public FieldChoiceState expanded(boolean expanded) {
        FieldChoiceState updated = new FieldChoiceState(this);
        updated.expanded = expanded;
        if(!expanded) {
            updated.draggingColumnId = Optional.empty();
            updated.draggingFieldId = Optional.empty();
        }
        return updated;
    }

    public FieldChoiceState fieldDragStart(String id) {
        if(draggingFieldId.isPresent() && draggingFieldId.get().equals(id)) {
            return this;
        }
        FieldChoiceState updated = new FieldChoiceState(this);
        updated.draggingFieldId = Optional.of(id);
        updated.draggingColumnId = Optional.empty();
        return updated;
    }

    public FieldChoiceState draggingEnd() {
        FieldChoiceState updated = new FieldChoiceState(this);
        updated.draggingFieldId = Optional.empty();
        updated.draggingColumnId = Optional.empty();
        return updated;
    }

    public FieldChoiceState withDropTarget(DropTarget dropTarget) {
        return withDropTarget(Optional.of(dropTarget));
    }

    public FieldChoiceState withDropTarget(Optional<DropTarget> dropTarget) {
        if(this.dropTarget.equals(dropTarget)) {
            return this;
        }
        FieldChoiceState updated = new FieldChoiceState(this);
        updated.dropTarget = dropTarget;
        return updated;
    }

    public boolean isDragging() {
        return draggingFieldId.isPresent() || draggingColumnId.isPresent();
    }

    public Optional<DropTarget> getDropTarget() {
        return dropTarget;
    }
}
