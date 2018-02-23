package chdc.frontend.client.table;

import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.table.view.ColumnSetProxy;

public class PerpColumn extends ActorColumn {

    public PerpColumn(FormSource formSource, ColumnSetProxy proxy) {
        super(formSource, proxy, "Perpetrator", "actor");
    }
}
