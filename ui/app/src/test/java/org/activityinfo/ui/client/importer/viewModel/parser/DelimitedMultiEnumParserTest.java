package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.Cardinality;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.enumerated.EnumValue;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DelimitedMultiEnumParserTest {

    @Test
    public void test() {
        ResourceId x = ResourceId.valueOf("X");
        ResourceId y = ResourceId.valueOf("Y");
        ResourceId z = ResourceId.valueOf("Z");
        EnumType enumType = new EnumType(Cardinality.MULTIPLE,
                new EnumItem(x, "Children"),
                new EnumItem(y, "Children and Parents"),
                new EnumItem(z, "Children, Parents, and Friends"));

        DelimitedMultiEnumParser parser = new DelimitedMultiEnumParser(enumType);
        assertThat(parser.parse("Children"), equalTo(new EnumValue(x)));
        assertThat(parser.parse("Children, Children and Parents"), equalTo(new EnumValue(x, y)));
        assertThat(parser.parse("Children, Children, Parents, and Friends"), equalTo(new EnumValue(x, z)));

        assertThat(parser.parse("CHILDREN, CHILDREN, PARENTS, AND FRIENDS"), equalTo(new EnumValue(x, z)));

    }

    @Test
    public void extraWhitespace() {

        ResourceId a = ResourceId.valueOf("t1490770869");
        ResourceId b = ResourceId.valueOf("t1491347993");
        ResourceId c = ResourceId.valueOf("t0733339025");
        ResourceId d = ResourceId.valueOf("t1329142954");
        ResourceId e = ResourceId.valueOf("t1958592155");
        EnumType enumType = new EnumType(Cardinality.MULTIPLE,
                new EnumItem(a, "Both parents"),
                        new EnumItem(b, "Other adult family member *seperated child)"),
                        new EnumItem(c, "Other adult, non-related (unaccompanied child) "),
                        new EnumItem(d, "Other minor / sibling aged 0- 17 years (unaccompanied child)"),
                        new EnumItem(e, "Alone"));

        DelimitedMultiEnumParser parser = new DelimitedMultiEnumParser(enumType);

        assertThat(parser.parse("Other adult, non-related (unaccompanied child)"), equalTo(new EnumValue(c)));
    }

}