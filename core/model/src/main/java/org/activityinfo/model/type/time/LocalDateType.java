package org.activityinfo.model.type.time;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.activityinfo.model.type.FieldType;
import org.activityinfo.model.type.FieldTypeClass;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.RecordFieldTypeClass;

/**
 * Value type that represents a date in the ISO-8601 calendar.
 * There is no representation of time-of-day or time-zone.
 *
 * <blockquote>
 * The “local” terminology is familiar from Joda-Time and comes originally from the ISO-8601 date and time standard.
 * It relates specifically to the absence of a time-zone. In effect, a local date is a description of a date,
 * such as the “5th April 2014”. That particular local date will start at different points on the time-line
 * depending on where on the Earth you are. Thus the local date will start in Australia 10 hours before it
 * starts in London and 18 hours before it starts in San Francisco. -- Stephen Colebourn in
 * <a href="http://www.infoq.com/articles/java.time">InfoQ</a>
 * </blockquote>
 *
 */
public class LocalDateType implements FieldType {

    public static final String TYPE_ID = "date";

    public static final FieldTypeClass TYPE_CLASS = new RecordFieldTypeClass() {
        @Override
        public String getId() {
            return TYPE_ID;
        }

        @Override
        public FieldType createType() {
            return INSTANCE;
        }

    };

    public static final LocalDateType INSTANCE = new LocalDateType();


    private LocalDateType() { }

    @Override
    public FieldTypeClass getTypeClass() {
        return TYPE_CLASS;
    }

    @Override
    public FieldValue parseJsonValue(JsonElement value) {
        if(value instanceof JsonObject) {
            value = ((JsonObject) value).getAsJsonPrimitive("value");
        }
        return LocalDate.parse(value.getAsString());
    }

    @Override
    public String toString() {
        return "LocalDateType";
    }

    /**
     *
     * @return the singleton instance for this type
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
