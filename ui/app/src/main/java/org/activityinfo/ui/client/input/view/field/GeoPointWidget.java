package org.activityinfo.ui.client.input.view.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.TextInputCell.TextFieldAppearance;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.type.FieldValue;

/**
 * Lays out latitude and longitude in a single row.
 */
public class GeoPointWidget implements FieldWidget {

    private static final TextFieldAppearance APPEARANCE = GWT.create(TextFieldAppearance.class);

    private final GeoPointEditor editor;
    private final Container panel;

    public GeoPointWidget(FieldUpdater updater) {

        editor = new GeoPointEditor(updater, APPEARANCE, APPEARANCE);

        panel = new FlowLayoutContainer();
        panel.add(new FieldLabel(editor.getLatitudeTextField(), I18N.CONSTANTS.latitude()));
        panel.add(new FieldLabel(editor.getLongitudeTextField(), I18N.CONSTANTS.longitude()));
    }

    @Override
    public void init(FieldValue value) {
        editor.init(value);
    }

    @Override
    public void clear() {
        editor.clear();
    }

    @Override
    public void setRelevant(boolean relevant) {
        editor.setRelevant(relevant);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }
}
