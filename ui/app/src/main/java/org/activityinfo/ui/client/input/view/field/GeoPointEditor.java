package org.activityinfo.ui.client.input.view.field;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.cell.core.client.form.TextInputCell;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.io.match.coord.CoordinateFormatException;
import org.activityinfo.io.match.coord.CoordinateParser;
import org.activityinfo.io.match.coord.JsCoordinateNumberFormatter;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.geo.GeoPoint;
import org.activityinfo.ui.client.input.model.FieldInput;


/**
 * FieldWidget for {@link org.activityinfo.model.type.geo.GeoPointType} fields.
 */
public class GeoPointEditor {

    private static final int DEBOUNCE_DELAY_MS = 100;

    private CoordField latitude;
    private CoordField longitude;
    private FieldUpdater updater;
    private final Timer timer;


    public GeoPointEditor(FieldUpdater updater,
                          TextInputCell.TextFieldAppearance latitudeAppearance,
                          TextInputCell.TextFieldAppearance longitudeAppearance) {
        this.updater = updater;
        latitude = new CoordField(CoordinateAxis.LATITUDE, latitudeAppearance);
        longitude = new CoordField(CoordinateAxis.LONGITUDE, longitudeAppearance);

        latitude.field.addKeyUpHandler(this::onKeyUp);
        longitude.field.addKeyUpHandler(this::onKeyUp);

        timer = new Timer() {
            @Override
            public void run() {
                updateModel();
            }
        };
    }

    public IsWidget getLatitudeTextField() {
        return latitude.field;
    }

    public IsWidget getLongitudeTextField() {
        return longitude.field;
    }

    private void onKeyUp(KeyUpEvent event) {

        if(timer.isRunning()) {
            timer.cancel();
        }
        timer.schedule(DEBOUNCE_DELAY_MS);
    }

    private void updateModel() {
        latitude.parseAndValidate();
        longitude.parseAndValidate();


        if(latitude.empty && longitude.empty) {
            // If both coordinates are empty, consider this to
            // be an empty field and clear the field validation messages
            // (the form may still mark this field as a whole if the field is
            //  required, but we don't have to deal with that here)
            latitude.field.clearInvalid();
            longitude.field.clearInvalid();
            updater.update(FieldInput.EMPTY);

        } else {

            // If there is *any* input in *either* field, then
            // either the field is VALID, or it's INVALID.

            if (latitude.valid && longitude.valid) {
                updater.update(new FieldInput(new GeoPoint(latitude.value, longitude.value)));

            } else {
                latitude.markInvalidIfEmpty();
                longitude.markInvalidIfEmpty();
                updater.update(FieldInput.INVALID_INPUT);
            }
        }
    }

    public void init(FieldValue value) {
        if(value instanceof GeoPoint) {
            GeoPoint pointValue = (GeoPoint) value;
            latitude.init(pointValue.getLatitude());
            longitude.init(pointValue.getLongitude());
        }
    }

    public void clear() {
        latitude.clear();
        longitude.clear();
    }

    public void setRelevant(boolean relevant) {
        latitude.field.setEnabled(relevant);
        longitude.field.setEnabled(relevant);
    }

    private static class CoordField {
        private final CoordinateParser parser;
        private final TextField field;

        private Double value;
        private boolean empty = true;
        private boolean valid;

        public CoordField(CoordinateAxis axis, TextInputCell.TextFieldAppearance appearance) {
            this.parser = new CoordinateParser(axis, JsCoordinateNumberFormatter.INSTANCE);
            this.field = new TextField(new TextInputCell(appearance));
            this.field.setValidateOnBlur(false);
            this.field.setAutoValidate(false);
        }

        public void init(double coordinateValue) {
            value = coordinateValue;
            valid = true;
            empty = false;
            field.setText(parser.format(coordinateValue));
            field.clearInvalid();
        }

        public void clear() {
            value = null;
            empty = true;
            field.clear();
            field.clearInvalid();
        }

        /**
         * Tries to parse the text into a valid coordinate, and updates our {@code coordinateValue}
         */
        private void parseAndValidate() {

            String textInput = field.getText();

            empty = Strings.isNullOrEmpty(textInput);
            if(empty) {
                value = null;
                valid = false;
            } else {
                try {
                    value = parser.parse(field.getText());
                    valid = true;

                    field.clearInvalid();

                } catch (CoordinateFormatException e) {
                    field.markInvalid(e.getMessage());
                    valid = false;
                }
            }
        }

        private void markInvalidIfEmpty() {
            if(empty) {
                field.markInvalid(I18N.CONSTANTS.noNumber());
            }
        }
    }
}
