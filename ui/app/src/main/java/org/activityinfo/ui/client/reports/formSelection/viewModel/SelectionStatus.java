package org.activityinfo.ui.client.reports.formSelection.viewModel;

/**
 * Describes the selection status of a {@link SelectionNode}
 */
public enum SelectionStatus {

    /**
     * This node and all its decendants are selected
     */
    ALL,

    /**
     * Neither this node nor any of its descendants are selected
     */
    NONE,

    /**
     * Either this node or one of its descendants are selected, but not all
     */
    SOME
}
