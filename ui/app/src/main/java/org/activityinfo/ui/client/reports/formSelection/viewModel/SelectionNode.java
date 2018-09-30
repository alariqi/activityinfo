package org.activityinfo.ui.client.reports.formSelection.viewModel;

import org.activityinfo.model.resource.ResourceId;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A node in the form selection tree.
 */
public class SelectionNode implements Comparable<SelectionNode> {

    ResourceId id;
    NodeType type;
    SelectionNode parent;
    String label;
    List<SelectionNode> children;

    SelectionNode() {
    }

    /**
     *
     * @return this node's id
     */
    public ResourceId getId() {
        return id;
    }

    /**
     * @return this node's parent in the {@link SelectionTree}
     */
    public SelectionNode getParent() {
        return parent;
    }

    /**
     *
     * @return true if this is a root node and has no parent.
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * @return the human-readable label of this node.
     */
    public String getLabel() {
        return label;
    }

    public NodeType getType() {
        return type;
    }

    public List<SelectionNode> getChildren() {
        if(children == null) {
            return Collections.emptyList();
        } else {
            return children;
        }
    }

    /**
     *
     * @return {@code true} if this node has no children.
     */
    public boolean isLeaf() {
        return children == null;
    }

    /**
     *
     * @return a set of the form ids of this node and any form or sub-form descendants.
     */
    public Set<ResourceId> findForms() {
        Set<ResourceId> formIds = new HashSet<>();
        collectForms(formIds);
        return formIds;
    }

    private void collectForms(Set<ResourceId> formIds) {
        if(type == NodeType.FORM || type == NodeType.SUBFORM) {
            formIds.add(id);
        }
        if(children != null) {
            for (SelectionNode child : children) {
                child.collectForms(formIds);
            }
        }
    }

    @Override
    public int compareTo(SelectionNode o) {
        if(this.getType() == o.getType()) {
            return this.getLabel().compareToIgnoreCase(o.getLabel());
        }  else {
            return this.getType().compareTo(o.getType());
        }
    }

    @Override
    public String toString() {
        return "FormSelectionItem{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }

}
