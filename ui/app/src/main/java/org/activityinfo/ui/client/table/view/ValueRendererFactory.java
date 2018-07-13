/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.table.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.io.match.coord.CoordinateParser;
import org.activityinfo.io.match.coord.JsCoordinateNumberFormatter;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.*;
import org.activityinfo.model.type.attachment.AttachmentType;
import org.activityinfo.model.type.attachment.AttachmentValue;
import org.activityinfo.model.type.barcode.BarcodeType;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.enumerated.EnumValue;
import org.activityinfo.model.type.expr.CalculatedFieldType;
import org.activityinfo.model.type.geo.GeoAreaType;
import org.activityinfo.model.type.geo.GeoPoint;
import org.activityinfo.model.type.geo.GeoPointType;
import org.activityinfo.model.type.number.Quantity;
import org.activityinfo.model.type.number.QuantityType;
import org.activityinfo.model.type.primitive.BooleanType;
import org.activityinfo.model.type.primitive.HasStringValue;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.model.type.subform.SubFormReferenceType;
import org.activityinfo.model.type.time.*;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.input.view.field.Blobs;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.Optional;

public class ValueRendererFactory {

    public interface Templates extends SafeHtmlTemplates {
        @Template("<a href=\"{0}\">{1}</a>")
        SafeHtml attachmentLink(SafeUri uri, String name);
    }

    private static final Templates TEMPLATES = GWT.create(Templates.class);

    private static class TextValueRenderer implements ValueRenderer {

        @Override
        public VTree render(RecordTree recordTree, FieldValue value) {
            return new VText((((HasStringValue) value).asString()));
        }
    }

    private static class SerialNumberRenderer implements ValueRenderer {

        private SerialNumberType fieldType;

        public SerialNumberRenderer(SerialNumberType fieldType) {
            this.fieldType = fieldType;
        }

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {

            SerialNumber serialNumber = ((SerialNumber) fieldValue);
            String serial = fieldType.format(serialNumber);

            return new VText(serial);
        }
    }

    private static class QuantityRenderer implements ValueRenderer {

        private QuantityType type;

        public QuantityRenderer(QuantityType type) {
            this.type = type;
        }

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {
            return new VText(((Quantity) fieldValue).getValue() + " " + type.getUnits());
        }
    }

    private static class GeoPointRenderer implements ValueRenderer {
        private final CoordinateParser latitude = new CoordinateParser(CoordinateAxis.LATITUDE, JsCoordinateNumberFormatter.INSTANCE);
        private final CoordinateParser longitude = new CoordinateParser(CoordinateAxis.LONGITUDE, JsCoordinateNumberFormatter.INSTANCE);

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {
            GeoPoint point = ((GeoPoint) fieldValue);

            return new VNode(HtmlTag.P,
                    new VText(latitude.formatAsDMS(point.getLatitude())),
                    new VNode(HtmlTag.BR),
                    new VText(longitude.formatAsDMS(point.getLongitude())));
        }
    }

    private static class DateRenderer implements ValueRenderer {


        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {

            LocalDate localDate = (LocalDate) fieldValue;

            return new VText(localDate.toString());
        }
    }

    private static class MonthRenderer implements ValueRenderer {

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {
            Month month = (Month) fieldValue;
            return new VText(I18N.MESSAGES.month(month.getFirstDayOfMonth().atMidnightInMyTimezone()));
        }
    }

    private static class WeekRenderer implements ValueRenderer {

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {
            EpiWeek week = (EpiWeek) fieldValue;
            return new VText(I18N.MESSAGES.week(week.getYear(), week.getWeekInYear()));
        }
    }

    private static class FortnightRenderer implements ValueRenderer {

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {
            FortnightValue fortnight = (FortnightValue) fieldValue;
            return new VText(I18N.MESSAGES.fortnightFormat(
                    fortnight.getYear(),
                    fortnight.getWeekInYear(),
                    fortnight.getWeekInYear() + 1));
        }
    }

    private static class EnumRenderer implements ValueRenderer {
        private EnumType type;

        public EnumRenderer(EnumType type) {
            this.type = type;
        }

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {
            EnumValue enumValue = (EnumValue) fieldValue;
            StringBuilder text = new StringBuilder();

            boolean needsComma = false;
            for (EnumItem item : type.getValues()) {
                if(enumValue.getResourceIds().contains(item.getId())) {
                    if(needsComma) {
                        text.append(", ");
                    }
                    text.append(item.getLabel());
                    needsComma = true;
                }
            }
            return new VText(text.toString());
        }
    }

    private static class AttachmentRenderer implements ValueRenderer {

        private final ResourceId formId;

        public AttachmentRenderer(ResourceId formId) {
            this.formId = formId;
        }

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {
            AttachmentValue attachments = (AttachmentValue) fieldValue;

            return new VNode(HtmlTag.DIV, attachments.getValues().stream().map(a ->
                    new VNode(HtmlTag.A,
                        Props.create().href(Blobs.getAttachmentUri(formId, a.getBlobId())),
                        new VText(a.getFilename()))));
        }
    }

    private static class ReferenceRenderer implements ValueRenderer {
        private final FormField field;
        private final LookupKeySet keySet;

        public ReferenceRenderer(FormTree formTree, FormField field) {
            this.field = field;
            this.keySet = new LookupKeySet(formTree, field);
        }

        @Override
        public VTree render(RecordTree recordTree, FieldValue fieldValue) {

            ReferenceValue refValue = (ReferenceValue) fieldValue;

            return new VNode(HtmlTag.UL, refValue.getReferences().stream().map(r -> {
                Maybe<String> label = keySet.label(recordTree, r);
                if(label.isVisible()) {
                    return new VNode(HtmlTag.LI, new VText(label.get()));
                } else {
                    return new VNode(HtmlTag.LI, new VText("#REF!"));
                }
            }));
        }
    }

    public static Optional<ValueRenderer> create(FormTree formTree, FormField field) {
        return field.getType().accept(new FieldTypeVisitor<Optional<ValueRenderer>>() {
            @Override
            public Optional<ValueRenderer> visitAttachment(AttachmentType attachmentType) {
                return Optional.of(new AttachmentRenderer(formTree.getRootFormId()));
            }

            @Override
            public Optional<ValueRenderer> visitCalculated(CalculatedFieldType calculatedFieldType) {
                return Optional.empty();
            }

            @Override
            public Optional<ValueRenderer> visitReference(ReferenceType referenceType) {
                return Optional.of(new ReferenceRenderer(formTree, field));
            }

            @Override
            public Optional<ValueRenderer> visitNarrative(NarrativeType narrativeType) {
                return Optional.of(new TextValueRenderer());
            }

            @Override
            public Optional<ValueRenderer> visitBoolean(BooleanType booleanType) {
                return Optional.empty();
            }

            @Override
            public Optional<ValueRenderer> visitQuantity(QuantityType type) {
                return Optional.of(new QuantityRenderer(type));
            }

            @Override
            public Optional<ValueRenderer> visitGeoPoint(GeoPointType geoPointType) {
                return Optional.of(new GeoPointRenderer());
            }

            @Override
            public Optional<ValueRenderer> visitGeoArea(GeoAreaType geoAreaType) {
                return Optional.empty();
            }

            @Override
            public Optional<ValueRenderer> visitEnum(EnumType enumType) {
                return Optional.of(new EnumRenderer(enumType));
            }

            @Override
            public Optional<ValueRenderer> visitBarcode(BarcodeType barcodeType) {
                return Optional.of(new TextValueRenderer());
            }

            @Override
            public Optional<ValueRenderer> visitSubForm(SubFormReferenceType subFormReferenceType) {
                return Optional.empty();
            }

            @Override
            public Optional<ValueRenderer> visitLocalDate(LocalDateType localDateType) {
                return Optional.of(new DateRenderer());
            }

            @Override
            public Optional<ValueRenderer> visitWeek(EpiWeekType epiWeekType) {
                return Optional.of(new WeekRenderer());
            }

            @Override
            public Optional<ValueRenderer> visitMonth(MonthType monthType) {
                return Optional.of(new MonthRenderer());
            }

            @Override
            public Optional<ValueRenderer> visitYear(YearType yearType) {
                return Optional.empty();
            }

            @Override
            public Optional<ValueRenderer> visitFortnight(FortnightType fortnightType) {
                return Optional.of(new FortnightRenderer());
            }

            @Override
            public Optional<ValueRenderer> visitLocalDateInterval(LocalDateIntervalType localDateIntervalType) {
                return Optional.empty();
            }

            @Override
            public Optional<ValueRenderer> visitText(TextType textType) {
                return Optional.of(new TextValueRenderer());
            }

            @Override
            public Optional<ValueRenderer> visitSerialNumber(SerialNumberType serialNumberType) {
                return Optional.of(new SerialNumberRenderer(serialNumberType));
            }
        });
    }

}
