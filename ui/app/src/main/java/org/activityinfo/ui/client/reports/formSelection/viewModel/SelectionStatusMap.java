package org.activityinfo.ui.client.reports.formSelection.viewModel;

import org.activityinfo.model.resource.ResourceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Calculates and stores the {@link SelectionStatus} of each {@link SelectionNode} in the
 * {@link SelectionTree}
 */
public class SelectionStatusMap {

    private static final Logger LOGGER = Logger.getLogger(SelectionStatusMap.class.getName());

    private final Set<ResourceId> selected;
    private SelectionTree tree;
    private Map<ResourceId, SelectionStatus> map = new HashMap<>();

    public SelectionStatusMap(SelectionTree tree, Set<ResourceId> selectedForms) {
        this.tree = tree;
        this.selected = selectedForms;

        LOGGER.info("selected = " + selectedForms);

        // Conduct a depth-first search to identify which nodes are
        // fully, partially, or not selected
        for (SelectionNode node : tree.getRoot().getChildren()) {
            visit(node);
        }
    }

    private void visit(SelectionNode node) {
        if(selected.contains(node.getId())) {
            visitSelected(node);
            visitParentsOfSelected(node);

        } else {
            for (SelectionNode child : node.getChildren()) {
                visit(child);
            }
        }
    }

    private void visitSelected(SelectionNode node) {
        map.put(node.getId(), SelectionStatus.ALL);
        for (SelectionNode childNode : node.getChildren()) {
            visitSelected(childNode);
        }
    }

    private void visitParentsOfSelected(SelectionNode node) {
        while(!node.isRoot()) {
            node = node.getParent();

            if(allChildrenSelected(node)) {
                map.put(node.getId(), SelectionStatus.ALL);

            } else {
                SelectionStatus previousState = map.put(node.getId(), SelectionStatus.SOME);
                if(previousState == SelectionStatus.SOME) {
                    break;
                }
            }
        }
    }

    private boolean allChildrenSelected(SelectionNode node) {
        for (SelectionNode child : node.getChildren()) {
            if(map.get(child.getId()) != SelectionStatus.ALL) {
                return false;
            }
        }
        return true;
    }

    public SelectionStatus isSelected(ResourceId resourceId) {
        return map.getOrDefault(resourceId, SelectionStatus.NONE);
    }

    public SelectionTree getTree() {
        return tree;
    }

}
