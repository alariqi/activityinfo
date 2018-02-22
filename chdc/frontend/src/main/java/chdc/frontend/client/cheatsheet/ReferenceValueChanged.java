package chdc.frontend.client.cheatsheet;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import org.activityinfo.ui.client.table.view.LabeledReference;

class ReferenceValueChanged extends ValueChangeEvent<LabeledReference> {

    /**
     * Creates a value change event.
     *
     * @param value the value
     */
    public ReferenceValueChanged(LabeledReference value) {
        super(value);
    }
}
