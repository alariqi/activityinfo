package org.activityinfo.ui.client.input.view.field;

import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
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
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.input.viewModel.PermissionFilters;

/**
 * Constructs a {@link FieldWidget} for a given field.
 */
public class FieldWidgetFactory  {

    private final FormSource formSource;
    private final FormTree formTree;
    private final PermissionFilters permissionFilters;

    public FieldWidgetFactory(FormSource formSource, FormTree formTree) {
        this.formSource = formSource;
        this.formTree = formTree;
        this.permissionFilters = new PermissionFilters(formTree);
    }

    public FieldWidget create(FormField field, FieldUpdater updater) {
        return field.getType().accept(new Visitor(field, updater));
    }

    private class Visitor implements FieldTypeVisitor<FieldWidget> {

        private FormField field;
        private FieldUpdater updater;

        public Visitor(FormField field, FieldUpdater updater) {
            this.field = field;
            this.updater = updater;
        }

        @Override
        public FieldWidget visitAttachment(AttachmentType attachmentType) {
            return new AttachmentWidget(formTree.getRootFormId(), updater);
        }

        @Override
        public FieldWidget visitCalculated(CalculatedFieldType calculatedFieldType) {
            return null;
        }

        @Override
        public FieldWidget visitReference(ReferenceType referenceType) {
            if (referenceType.getRange().size() != 1) {
                return null;
            }

            return new ReferenceFieldWidget(formSource, formTree, field, permissionFilters, updater);
        }

        @Override
        public FieldWidget visitNarrative(NarrativeType narrativeType) {
            return new NarrativeWidget(updater);
        }

        @Override
        public FieldWidget visitBoolean(BooleanType booleanType) {
            return null;
        }

        @Override
        public FieldWidget visitQuantity(QuantityType type) {
            return new QuantityWidget(type, updater);
        }

        @Override
        public FieldWidget visitGeoPoint(GeoPointType geoPointType) {
            return new GeoPointWidget(updater);
        }

        @Override
        public FieldWidget visitGeoArea(GeoAreaType geoAreaType) {
            return null;
        }

        @Override
        public FieldWidget visitEnum(EnumType enumType) {
            if (enumType.getCardinality() == Cardinality.SINGLE) {
                // If the field is optional, then ALWAYS use a drop down widget whose value can be cleared
                if (!field.isRequired()) {
                    return new DropDownEnumWidget(field, enumType, updater);

                } else if (enumType.getEffectivePresentation() == EnumType.Presentation.RADIO_BUTTON) {
                    return new RadioGroupWidget(enumType, updater);

                } else {
                    return new DropDownEnumWidget(field, enumType, updater);
                }
            } else {
                return new CheckBoxGroupWidget(enumType, updater);
            }
        }

        @Override
        public FieldWidget visitBarcode(BarcodeType barcodeType) {
            return new BarcodeWidget(updater);
        }

        @Override
        public FieldWidget visitSubForm(SubFormReferenceType subFormReferenceType) {
            return null;
        }

        @Override
        public FieldWidget visitLocalDate(LocalDateType localDateType) {
            return new LocalDateWidget(updater);
        }

        @Override
        public FieldWidget visitWeek(EpiWeekType epiWeekType) {
            return new WeekWidget(updater);
        }

        @Override
        public FieldWidget visitMonth(MonthType monthType) {
            return new MonthWidget(updater);
        }

        @Override
        public FieldWidget visitYear(YearType yearType) {
            return null;
        }

        @Override
        public FieldWidget visitFortnight(FortnightType fortnightType) {
            return new FortnightWidget(updater);
        }

        @Override
        public FieldWidget visitLocalDateInterval(LocalDateIntervalType localDateIntervalType) {
            return null;
        }

        @Override
        public FieldWidget visitText(TextType textType) {
            return new TextWidget(textType, updater);
        }

        @Override
        public FieldWidget visitSerialNumber(SerialNumberType serialNumberType) {
            return new SerialNumberWidget(serialNumberType);
        }

    }
}
