package org.activityinfo.ui.client.importer.viewModel;

import com.google.gwt.core.client.JavaScriptObject;
import org.activityinfo.api.client.ColumnArrayView;
import org.activityinfo.model.query.ColumnType;
import org.activityinfo.model.query.ColumnView;

public final class JsColumnBuilder extends JavaScriptObject implements ColumnBuilder {

    public static native JsColumnBuilder create() /*-{
        return [];
    }-*/;

    protected JsColumnBuilder() {
    }

    @Override
    public native void add(String value) /*-{
        if(value === "") {
            this.push(null);
        } else {
            this.push(value);
        }
    }-*/;

    @Override
    public ColumnView build() {
        return new ColumnArrayView(ColumnType.STRING, this);
    }
}
