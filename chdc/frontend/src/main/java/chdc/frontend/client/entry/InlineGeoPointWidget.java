package chdc.frontend.client.entry;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.ui.client.input.view.field.FieldUpdater;
import org.activityinfo.ui.client.input.view.field.FieldWidget;
import org.activityinfo.ui.client.input.view.field.GeoPointEditor;

import java.util.Arrays;
import java.util.Collection;

public class InlineGeoPointWidget implements FieldWidget, MultiFieldWidget {

    private GeoPointEditor editor;

    public InlineGeoPointWidget(FieldUpdater updater) {
        this.editor = new GeoPointEditor(updater,
                new TextAppearance(I18N.CONSTANTS.latitude()),
                new TextAppearance(I18N.CONSTANTS.longitude()));

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
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<IsWidget> getWidgets() {
        return Arrays.asList(editor.getLatitudeTextField(), editor.getLongitudeTextField());
    }
}
