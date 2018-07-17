package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.type.FieldTypeVisitor;
import org.activityinfo.model.type.NarrativeType;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.model.type.SerialNumberType;
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
import org.activityinfo.ui.client.importer.viewModel.parser.*;
import org.activityinfo.ui.client.store.FormStore;

import java.util.Optional;

public class FieldViewModelFactory implements FieldTypeVisitor<Optional<FieldViewModel>> {

    private final FormStore formStore;
    private final FormTree formTree;
    private final FormField field;

    public static Optional<FieldViewModel> create(FormStore formStore, FormTree formTree, FormField field) {
        return field.getType().accept(new FieldViewModelFactory(formStore, formTree, field));
    }

    private FieldViewModelFactory(FormStore formStore, FormTree formTree, FormField field) {
        this.formStore = formStore;
        this.formTree = formTree;
        this.field = field;
    }

    @Override
    public Optional<FieldViewModel> visitAttachment(AttachmentType attachmentType) {
        return Optional.empty();
    }

    @Override
    public Optional<FieldViewModel> visitCalculated(CalculatedFieldType calculatedFieldType) {
        return Optional.empty();
    }

    @Override
    public Optional<FieldViewModel> visitReference(ReferenceType referenceType) {
        return Optional.of(new ReferenceViewModel(formStore, formTree, field));
    }

    @Override
    public Optional<FieldViewModel> visitNarrative(NarrativeType narrativeType) {
        return Optional.of(new SimpleFieldViewModel(field, new NarrativeParser()));
    }

    @Override
    public Optional<FieldViewModel> visitBoolean(BooleanType booleanType) {
        return Optional.empty();
    }

    @Override
    public Optional<FieldViewModel> visitQuantity(QuantityType type) {
        return Optional.of(new SimpleFieldViewModel(field, new QuantityParser()));
    }

    @Override
    public Optional<FieldViewModel> visitGeoPoint(GeoPointType geoPointType) {
        return Optional.of(new GeoPointViewModel(field));
    }

    @Override
    public Optional<FieldViewModel> visitGeoArea(GeoAreaType geoAreaType) {
        return Optional.empty();
    }

    @Override
    public Optional<FieldViewModel> visitEnum(EnumType enumType) {
        return Optional.of(new SimpleFieldViewModel(field, new EnumParser(enumType)));
    }

    @Override
    public Optional<FieldViewModel> visitBarcode(BarcodeType barcodeType) {
        return Optional.of(new SimpleFieldViewModel(field, new BarcodeParser()));
    }

    @Override
    public Optional<FieldViewModel> visitSubForm(SubFormReferenceType subFormReferenceType) {
        return Optional.empty();
    }

    @Override
    public Optional<FieldViewModel> visitLocalDate(LocalDateType localDateType) {
        return Optional.of(new SimpleFieldViewModel(field, new LocalDateParser()));
    }

    @Override
    public Optional<FieldViewModel> visitMonth(MonthType monthType) {
        return Optional.of(new SimpleFieldViewModel(field, new MonthParser()));
    }

    @Override
    public Optional<FieldViewModel> visitYear(YearType yearType) {
        return Optional.of(new SimpleFieldViewModel(field, new YearParser()));

    }

    @Override
    public Optional<FieldViewModel> visitFortnight(FortnightType fortnightType) {
        return Optional.of(new SimpleFieldViewModel(field, new FortnightParser()));
    }

    @Override
    public Optional<FieldViewModel> visitWeek(EpiWeekType epiWeekType) {
        return Optional.of(new SimpleFieldViewModel(field, new WeekParser()));
    }

    @Override
    public Optional<FieldViewModel> visitLocalDateInterval(LocalDateIntervalType localDateIntervalType) {
        return Optional.empty();
    }

    @Override
    public Optional<FieldViewModel> visitText(TextType textType) {
        return Optional.of(new SimpleFieldViewModel(field, new TextParser()));

    }

    @Override
    public Optional<FieldViewModel> visitSerialNumber(SerialNumberType serialNumberType) {
        return Optional.empty();
    }

}
