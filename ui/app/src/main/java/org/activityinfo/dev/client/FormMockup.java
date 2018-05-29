package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
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

        DropDownEnumWidget textWidget = new DropDownEnumWidget(field("Partner"),
                partnerType(), NULL_UPDATER);
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
        householdView.invalidate(I18N.CONSTANTS.requiredFieldMessage());

        GeoPointWidget geoPointWidget = new GeoPointWidget(NULL_UPDATER);
        FieldView geoPointView = new FieldView(field("Precise location of the distribution"), geoPointWidget);

        GeoPointWidget invalidGeoPointWidget = new GeoPointWidget(NULL_UPDATER);
        FieldView invalidGeoPointView = new FieldView(field("Location of the upstream source"), invalidGeoPointWidget);
        invalidGeoPointView.invalidate(I18N.CONSTANTS.requiredFieldMessage());

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


        RadioGroupWidget radioWidget = new RadioGroupWidget(enumType(Cardinality.SINGLE,"Female", "Male"), NULL_UPDATER);
        FieldView radioView = new FieldView(requiredField("Gender"), radioWidget);

        CssLayoutContainer form = new CssLayoutContainer("form");
        form.add(serialNumberView);
        form.add(textView);
        form.add(codeView);
        form.add(dateView);
        form.add(commentsView);
        form.add(householdView);
        form.add(geoPointView);
        form.add(invalidGeoPointView);
        form.add(attachmentView);
        form.add(multiView);
        form.add(radioView);

        CssLayoutContainer inner = new CssLayoutContainer();
        inner.addStyleName("forminput__inner");
        inner.add(form);

        container = new CssLayoutContainer();
        container.addStyleName(DevBundle.RESOURCES.style().forms());
        container.add(inner);

    }

    private EnumType partnerType() {
        return enumType(Cardinality.SINGLE,
                "ACTED",
                "ADRA",
                "AFKAR",
                "Al-Amal",
                "Al-Ansar",
                "Al-Awg",
                "Al-Eslah",
                "Al-Hboby",
                "Al-Huda",
                "Al-Khair ",
                "Al-Khanjar",
                "Al-Masala",
                "Al-Mortaqa",
                "Al-Mustaqbal",
                "Al-Nahrain",
                "Al-pha",
                "Al-Raja",
                "Al-Rasad",
                "Al-Zuhoor",
                "AMAR",
                "AMTNA",
                "Anamal Al-Khair",
                "Anhur",
                "Anwar Al-Mustaqbal",
                "AOTJ",
                "Arche Nova",
                "ASB",
                "ASFL",
                "Asuda",
                "AVSI",
                "Aynda",
                "Baghdad",
                "BCF",
                "BHO",
                "BIC",
                "BO",
                "BORDA",
                "Bothoor Al-Khaer",
                "BRHA",
                "BROB",
                "BWA",
                "CAOFISR",
                "CAPNI",
                "CARE",
                "Caritas-Czech",
                "Caritas-Iraq",
                "CDE",
                "CDO",
                "Chavin",
                "CHF",
                "Christian Aid",
                "CNSF",
                "COCC",
                "COOPI",
                "Cordaid",
                "CRS",
                "CSI",
                "DAA",
                "Dabin",
                "DAD",
                "DAI",
                "DAMA",
                "DARY",
                "DCA",
                "DCVAW",
                "DDG",
                "Default",
                "DESW",
                "DHRD",
                "DIGC",
                "Dijlata Al-kair ",
                "DoE",
                "DoE Dohuk",
                "DoE Erbil",
                "DoE Kirkuk",
                "DoE Suli",
                "DoGOW",
                "DoH Anbar",
                "DoH Baghdad",
                "DoH Duhok",
                "DoH Erbil",
                "DoH Kirkuk",
                "DoH Ninewa",
                "DoH Salaha al-din",
                "DoH Sulaymeniyah",
                "DoLSA Dahuk",
                "DoLSA Erbil",
                "DoLSA Garmyan",
                "DoLSA Sulaymaniyah",
                "DoM Kirkuk",
                "DOST",
                "DoSW Sulaymeniyah",
                "DoW Dohok",
                "DoW Erbil",
                "DoW Kirkuk",
                "DoW Sulaymeniyah",
                "DRC",
                "DW",
                "EAC",
                "EADE",
                "ECHO",
                "Edge",
                "Education Cluster",
                "EJCC",
                "Emergency",
                "Emma",
                "EORD",
                "ERC",
                "FAO",
                "Fatema House",
                "FEI",
                "FOCSIV",
                "Food Security Cluster",
                "FPA",
                "FRC");

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
