package org.activityinfo.model.form;

import org.activityinfo.model.type.FieldTypeClass;
import org.activityinfo.model.type.TypeRegistry;

/**
 * The type of field, which influences how input is presented
 * the user, how it is validated, and what default measures
 * are available.
 */
public class FormFieldType {

    private FormFieldType() {}


    /**
     * Defined exact length of string to differ between FREE_TEXT and NARRATIVE types.
     * If string length less than #FREE_TEXT_LENGTH then type is #FREE_TEXT otherwise it is NARRATIVE.
     */
    public static final int FREE_TEXT_LENGTH = 80;


    public static FieldTypeClass valueOf(String name) {
        return TypeRegistry.get().getTypeClass(name);
    }

    public static FieldTypeClass[] values() {
        return new FieldTypeClass[] {
                FieldTypeClass.QUANTITY,
                FieldTypeClass.NARRATIVE,
                FieldTypeClass.FREE_TEXT,
                FieldTypeClass.LOCAL_DATE,
                FieldTypeClass.BOOLEAN,
                FieldTypeClass.GEOGRAPHIC_POINT,
                FieldTypeClass.BARCODE};

    }
}
