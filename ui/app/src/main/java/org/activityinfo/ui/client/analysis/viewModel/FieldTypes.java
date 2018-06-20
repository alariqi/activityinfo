package org.activityinfo.ui.client.analysis.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.type.*;
import org.activityinfo.model.type.attachment.AttachmentType;
import org.activityinfo.model.type.barcode.BarcodeType;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.expr.CalculatedFieldType;
import org.activityinfo.model.type.geo.GeoAreaType;
import org.activityinfo.model.type.geo.GeoPointType;
import org.activityinfo.model.type.number.QuantityType;
import org.activityinfo.model.type.primitive.BooleanType;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.model.type.subform.SubFormReferenceType;
import org.activityinfo.model.type.time.*;

public class FieldTypes implements FieldTypeVisitor<String> {

    private static final FieldTypes INSTANCE = new FieldTypes();

    private FieldTypes() {}

    public static String localizedFieldType(FieldType type) {
        return type.accept(INSTANCE);
    }

    @Override
    public String visitAttachment(AttachmentType attachmentType) {
        return I18N.CONSTANTS.attachment();
    }

    @Override
    public String visitCalculated(CalculatedFieldType calculatedFieldType) {
        return I18N.CONSTANTS.fieldTypeCalculated();
    }

    @Override
    public String visitReference(ReferenceType referenceType) {
        return I18N.CONSTANTS.reference();
    }

    @Override
    public String visitNarrative(NarrativeType narrativeType) {
        return I18N.CONSTANTS.fieldTypeNarrative();
    }

    @Override
    public String visitBoolean(BooleanType booleanType) {
        return I18N.CONSTANTS.booleanFieldType();
    }

    @Override
    public String visitQuantity(QuantityType type) {
        return I18N.CONSTANTS.fieldTypeQuantity();
    }

    @Override
    public String visitGeoPoint(GeoPointType geoPointType) {
        return I18N.CONSTANTS.fieldTypeGeographicPoint();
    }

    @Override
    public String visitGeoArea(GeoAreaType geoAreaType) {
        return I18N.CONSTANTS.fieldTypeGeographicArea();
    }

    @Override
    public String visitEnum(EnumType enumType) {
        if(enumType.getCardinality() == Cardinality.MULTIPLE) {
            return I18N.CONSTANTS.multiple();
        } else {
            return I18N.CONSTANTS.select();
        }
    }

    @Override
    public String visitBarcode(BarcodeType barcodeType) {
        return I18N.CONSTANTS.fieldTypeBarcode();
    }

    @Override
    public String visitSubForm(SubFormReferenceType subFormReferenceType) {
        return I18N.CONSTANTS.subForm();
    }

    @Override
    public String visitLocalDate(LocalDateType localDateType) {
        return I18N.CONSTANTS.fieldTypeDate();
    }

    @Override
    public String visitMonth(MonthType monthType) {
        return I18N.CONSTANTS.month();
    }

    @Override
    public String visitYear(YearType yearType) {
        return I18N.CONSTANTS.year();
    }

    @Override
    public String visitFortnight(FortnightType fortnightType) {
        return I18N.CONSTANTS.fortnight();
    }

    @Override
    public String visitWeek(EpiWeekType epiWeekType) {
        return I18N.CONSTANTS.weekFieldLabel();
    }

    @Override
    public String visitLocalDateInterval(LocalDateIntervalType localDateIntervalType) {
        return I18N.CONSTANTS.date();
    }

    @Override
    public String visitText(TextType textType) {
        return I18N.CONSTANTS.fieldTypeText();
    }

    @Override
    public String visitSerialNumber(SerialNumberType serialNumberType) {
        return I18N.CONSTANTS.serialNumber();
    }
}
