package org.activityinfo.model.type.number;

import com.google.common.base.Strings;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.JsonParsing;
import org.activityinfo.model.type.*;

import static org.activityinfo.json.Json.createObject;

/**
 * A value types that describes a real-valued quantity and its units.
 */
public class QuantityType implements ParametrizedFieldType {


    public static class TypeClass implements ParametrizedFieldTypeClass, RecordFieldTypeClass {

        private TypeClass() {}

        @Override
        public String getId() {
            return "quantity";
        }

        @Override
        public QuantityType createType() {
            return new QuantityType()
                    .setUnits(I18N.CONSTANTS.defaultQuantityUnits());
        }

        @Override
        public FieldType deserializeType(JsonValue parametersObject) {
            String units = JsonParsing.toNullableString(parametersObject.get("units"));

            // handling for legacy QuantityType fields (w/ null aggregation) - default to SUM
            String aggregationString = Aggregation.SUM.name();
            if(parametersObject.hasKey("aggregation")) {
                aggregationString = parametersObject.getString("aggregation");
            }
            Aggregation aggregation = Aggregation.valueOf(aggregationString);
            return new QuantityType(units, aggregation);
        }

    }

    public enum Aggregation {
        SUM,
        AVERAGE,
        COUNT
    }

    public static final String UNKNOWN_UNITS = "unknown";
    public static final TypeClass TYPE_CLASS = new TypeClass();

    private String units;
    private Aggregation aggregation = Aggregation.SUM;

    public QuantityType() {
    }

    public QuantityType(String units) {
        this.units = units;
    }

    public QuantityType(String units, String aggregation) {
        this(units);
        this.aggregation = Aggregation.valueOf(aggregation);
    }

    public QuantityType(String units, Aggregation aggregation) {
        this(units);
        this.aggregation = aggregation;
    }

    public String getUnits() {
        return units;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public QuantityType setUnits(String units) {
        this.units = units;
        return this;
    }

    public QuantityType setAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
        return this;
    }

    public QuantityType setAggregation(String aggregation) {
        this.aggregation = Aggregation.valueOf(aggregation);
        return this;
    }

    public QuantityType setAggregation(int aggregation) {
        switch (aggregation) {
            case 0:
                this.aggregation = Aggregation.SUM;
                break;
            case 1:
                this.aggregation = Aggregation.AVERAGE;
                break;
            case 2:
                this.aggregation = Aggregation.COUNT;
                break;
        }
        return this;
    }

    /**
     * @return new QuantityType with the given {@code updatedUnits}
     */
    public QuantityType withUnits(String updatedUnits) {
        return new QuantityType(updatedUnits);
    }

    @Override
    public ParametrizedFieldTypeClass getTypeClass() {
        return TYPE_CLASS;
    }

    @Override
    public FieldValue parseJsonValue(JsonValue value) {
        double doubleValue = value.asNumber();
        if(Double.isNaN(doubleValue)) {
            throw new IllegalArgumentException();
        }
        return new Quantity(doubleValue);
    }

    @Override
    public <T> T accept(FieldTypeVisitor<T> visitor) {
        return visitor.visitQuantity(this);
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public JsonValue getParametersAsJson() {
        JsonValue object = createObject();
        object.put("units", Strings.nullToEmpty(units));
        object.put("aggregation", aggregation.toString());
        return object;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        return "QuantityType";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuantityType type = (QuantityType) o;

        return units != null ? units.equals(type.units) : type.units == null;

    }

    @Override
    public int hashCode() {
        return units != null ? units.hashCode() : 0;
    }
}
