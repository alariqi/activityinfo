package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.Cardinality;
import org.activityinfo.model.type.SerialNumberType;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.number.QuantityType;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.input.view.field.*;

import java.util.ArrayList;
import java.util.List;

public class FormMockup implements IsWidget {

    private final CssLayoutContainer container;

    private static final FieldUpdater NULL_UPDATER = input -> {};

    public FormMockup() {

        SerialNumberWidget serialNumberWidget = new SerialNumberWidget(new SerialNumberType(null, 5));
        FieldView serialNumberView = new FieldView(field("Serial number"), serialNumberWidget);

        TextWidget textWidget = new TextWidget(TextType.SIMPLE, NULL_UPDATER);
        FieldView textView = new FieldView(field("Partner"), textWidget);

        TextWidget codeWidget = new TextWidget(TextType.SIMPLE.withInputMask("000-0000"), NULL_UPDATER);
        FieldView codeView = new FieldView(requiredField("Family Registration Code"), codeWidget);

        LocalDateWidget dateWidget = new LocalDateWidget(NULL_UPDATER);
        FieldView dateView = new FieldView(field("Start date of distribution",
                "This is a short description attached to a field that people have to fill in. " +
                "It's important this is read and done correctly."), dateWidget);

        NarrativeWidget commentsWidget = new NarrativeWidget(NULL_UPDATER);
        FieldView commentsView = new FieldView(field("Comments"), commentsWidget);

        QuantityWidget householdWidget = new QuantityWidget(new QuantityType("households"), NULL_UPDATER);
        FieldView householdView = new FieldView(requiredField("Households served"), householdWidget);

        GeoPointWidget geoPointWidget = new GeoPointWidget(NULL_UPDATER);
        FieldView geoPointView = new FieldView(field("Precise location of the distribution"), geoPointWidget);

        AttachmentWidget attachmentWidget = new AttachmentWidget(ResourceId.generateId(), NULL_UPDATER);
        FieldView attachmentView = new FieldView(field("Certificate of distribution",
                "Please attach a scan of the signed copy of the certificate of distribution"), attachmentWidget);


        CheckBoxGroupWidget multiWidget = new CheckBoxGroupWidget(enumType(Cardinality.MULTIPLE,
                "Barrier related to agency/organization policy and practices",
                "Barrier related to gaps in capacity/service",
                "Barrier related to personal safety/security risks",
                "Barrier related to geographical isolation and/or other forms of inaccessibility not " +
                        "related to safety/security risks",
                "Other specify"), NULL_UPDATER);

        FieldView multiView = new FieldView(field("2.1 Type of access to services issue"), multiWidget);

        CssLayoutContainer form = new CssLayoutContainer("form");
        form.addStyleName("forminput__inner");
        form.add(serialNumberView);
        form.add(textView);
        form.add(codeView);
        form.add(dateView);
        form.add(commentsView);
        form.add(householdView);
        form.add(geoPointView);
        form.add(attachmentView);
        form.add(multiView);

        container = new CssLayoutContainer();
        container.addStyleName(DevBundle.RESOURCES.style().forms());
        container.add(form);

    }

    private EnumType enumType(Cardinality cardinality, String... labels) {
        List<EnumItem> items = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            items.add(new EnumItem(ResourceId.valueOf("e" + i), labels[i]));
        }
        return new EnumType(cardinality, items);
    }

    private FormField field(String label, String description) {
        FormField field = field(label);
        field.setDescription(description);
        return field;
    }

    private FormField field(String label) {
        FormField field = new FormField(ResourceId.valueOf("dummy"));
        field.setLabel(label);
        return field;
    }

    private FormField requiredField(String label) {
        FormField field = field(label);
        field.setRequired(true);
        return field;
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
