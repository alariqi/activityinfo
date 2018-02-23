package chdc.frontend.client.cheatsheet;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;

class ReferenceValueChanged extends ValueChangeEvent<LabeledRecordRef> {

    /**
     * Creates a value change event.
     *
     * @param value the value
     */
    public ReferenceValueChanged(LabeledRecordRef value) {
        super(value);
    }
}
