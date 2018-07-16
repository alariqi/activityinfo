package org.activityinfo.ui.client.importer.viewModel.targets;

import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.model.form.FormField;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ColumnTargetFactory implements FieldTypeVisitor<List<ColumnTarget>> {

    private final FormField field;

    public static List<ColumnTarget> create(FormField field) {
        return field.getType().accept(new ColumnTargetFactory(field));
    }

    private ColumnTargetFactory(FormField field) {
        this.field = field;
    }

    @Override
    public List<ColumnTarget> visitAttachment(AttachmentType attachmentType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitCalculated(CalculatedFieldType calculatedFieldType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitReference(ReferenceType referenceType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitNarrative(NarrativeType narrativeType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitBoolean(BooleanType booleanType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitQuantity(QuantityType type) {
        return Collections.singletonList(new QuantityTarget(field));
    }

    @Override
    public List<ColumnTarget> visitGeoPoint(GeoPointType geoPointType) {
        return Arrays.asList(
                new CoordTarget(field, CoordinateAxis.LATITUDE),
                new CoordTarget(field, CoordinateAxis.LONGITUDE));
    }

    @Override
    public List<ColumnTarget> visitGeoArea(GeoAreaType geoAreaType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitEnum(EnumType enumType) {
        if(enumType.getCardinality() == Cardinality.SINGLE) {
            return Collections.singletonList(new SingleEnumTarget(field));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ColumnTarget> visitBarcode(BarcodeType barcodeType) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public List<ColumnTarget> visitSubForm(SubFormReferenceType subFormReferenceType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitLocalDate(LocalDateType localDateType) {
        return Collections.singletonList(new DateTarget(field));
    }

    @Override
    public List<ColumnTarget> visitMonth(MonthType monthType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitYear(YearType yearType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitFortnight(FortnightType fortnightType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitWeek(EpiWeekType epiWeekType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitLocalDateInterval(LocalDateIntervalType localDateIntervalType) {
        return Collections.emptyList();
    }

    @Override
    public List<ColumnTarget> visitText(TextType textType) {
        return Collections.singletonList(new TextTarget(field));
    }

    @Override
    public List<ColumnTarget> visitSerialNumber(SerialNumberType serialNumberType) {
        return Collections.emptyList();
    }
}
