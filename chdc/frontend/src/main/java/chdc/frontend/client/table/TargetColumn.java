package chdc.frontend.client.table;

import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

public class TargetColumn extends ActorColumn {


    public TargetColumn(FormSource formSource, ColumnSetProxy proxy) {
        super(formSource, proxy, "Primary Target", "primary_target");
    }
}
