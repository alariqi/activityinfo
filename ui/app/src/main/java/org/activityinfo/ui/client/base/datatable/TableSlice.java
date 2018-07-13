package org.activityinfo.ui.client.base.datatable;

import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.Arrays;
import java.util.Optional;

public class TableSlice {

    public static final TableSlice EMPTY = new TableSlice(0, 0, H.tableBody());

    private int startIndex;
    private int totalRowCount;
    private VNode tableBody;

    public TableSlice(int startIndex, int totalRowCount, VNode tableBody) {
        this.startIndex = startIndex;
        this.totalRowCount = totalRowCount;
        this.tableBody = tableBody;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getTotalRowCount() {
        return totalRowCount;
    }

    public VTree getTableBody() {
        return tableBody;
    }

    /**
     * Creates a new TableSlice with the given row updated to include a selection class name.
     *
     * <p>If a selection is present, and the row is included in this slice, it copy of this
     * TableSlice will be made with an updated row. Otherwise, a reference to this TableSlice is returned.</p>
     */
    public TableSlice applySelectionClass(Optional<String> selectedRowId) {
        if(!selectedRowId.isPresent()) {
            return this;
        }
        String selectedId = selectedRowId.get();

        for (int i = 0; i < tableBody.children.length; i++) {
            VNode row = (VNode) tableBody.children[i];
            String rowId = row.key;
            if(selectedId.equals(rowId)) {
                return applySelection(i);
            }
        }

        return this;
    }


    private TableSlice applySelection(int selectedIndex) {
        VTree[] rows = Arrays.copyOf(tableBody.children, tableBody.children.length);

        VNode row = (VNode) tableBody.children[selectedIndex];

        PropMap selectedRowProps = Props.create();
        selectedRowProps.setData("row", row.key);
        selectedRowProps.setClass("selected");

        VNode selectedRow = new VNode(row.tag, selectedRowProps, row.children, row.key);

        rows[selectedIndex] = selectedRow;

        return new TableSlice(startIndex, totalRowCount,
            new VNode(HtmlTag.TBODY, tableBody.properties, rows));
    }
}
