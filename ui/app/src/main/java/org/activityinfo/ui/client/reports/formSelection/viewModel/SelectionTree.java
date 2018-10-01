package org.activityinfo.ui.client.reports.formSelection.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.database.Resource;
import org.activityinfo.model.database.ResourceType;
import org.activityinfo.model.database.UserDatabaseMeta;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.reports.formSelection.state.FormSelectionState;

import java.util.*;

/**
 * A tree of all selectable databases, reports, folders, forms, and subforms that are
 * visible to the user.
 *
 */
public class SelectionTree {

    private SelectionNode root;

    private Map<ResourceId, SelectionNode> nodeMap = new HashMap<>();

    public SelectionTree(List<UserDatabaseMeta> databases) {

        root = new SelectionNode();
        root.type = NodeType.ROOT;

        SelectionNode databaseRoot = new SelectionNode();
        databaseRoot.id = FormSelectionState.DATABASE_ROOT_ID;
        databaseRoot.type = NodeType.ROOT;
        databaseRoot.label = I18N.CONSTANTS.databases();
        add(databaseRoot);
        addChild(root, databaseRoot);

        SelectionNode reportRoot = new SelectionNode();
        reportRoot.id = FormSelectionState.REPORTS_ROOT_ID;
        reportRoot.type = NodeType.ROOT;
        reportRoot.label = I18N.CONSTANTS.reports();
        add(reportRoot);
        addChild(root, reportRoot);

        for (UserDatabaseMeta database : databases) {
            SelectionNode databaseNode = new SelectionNode();
            databaseNode.id = database.getDatabaseId();
            databaseNode.label = database.getLabel();
            databaseNode.type = NodeType.DATABASE;
            add(databaseNode);
            addChild(databaseRoot, databaseNode);

            for (Resource resource : database.getResources()) {
                SelectionNode node = new SelectionNode();
                node.id = resource.getId();
                node.label = resource.getLabel();
                node.type = nodeType(resource.getType());
                add(node);
            }

            for (Resource resource : database.getResources()) {
                SelectionNode node = nodeMap.get(resource.getId());
                SelectionNode parentNode = nodeMap.get(resource.getParentId());
                if(parentNode != null && node != null) {
                    addChild(parentNode, node);
                }
            }
        }

        // Sort children by type and then by name
        for (SelectionNode node : nodeMap.values()) {
            if(!node.isLeaf() && node != root) {
                Collections.sort(node.children);
            }
        }
    }

    private void add(SelectionNode node) {
        nodeMap.put(node.getId(), node);
    }

    private void addChild(SelectionNode parent, SelectionNode child) {
        child.parent = parent;
        if(parent.children == null) {
            parent.children = new ArrayList<>();
        }
        parent.children.add(child);
    }

    private NodeType nodeType(ResourceType type) {
        if(type == ResourceType.FOLDER) {
            return NodeType.FOLDER;
        } else {
            return NodeType.FORM;
        }
    }

    public SelectionNode getRoot() {
        return root;
    }

    public SelectionNode getNode(ResourceId nodeId) {
        return nodeMap.get(nodeId);
    }
}
