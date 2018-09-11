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
package org.activityinfo.store.query.shared;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormClassProvider;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.FormTreeBuilder;
import org.activityinfo.model.formTree.FormTreePrettyPrinter;
import org.activityinfo.model.formula.CompoundExpr;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.formula.FormulaParser;
import org.activityinfo.model.formula.SymbolNode;
import org.activityinfo.model.formula.diagnostic.AmbiguousSymbolException;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.Cardinality;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.geo.GeoPointType;
import org.activityinfo.model.type.primitive.TextType;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class NodeMatcherTest {

    private FormClass rootFormClass;
    private Map<ResourceId, FormClass> formClasses = Maps.newHashMap();
    private NodeMatcher symbolTable;

    @Test
    public void basicForm() {
        givenRootForm("Contact Form", field("A", "Name"), field("B", "Phone Number"));

        assertThat(resolve("A"), contains("A"));
        assertThat(resolve("Name"), contains("A"));
        assertThat(resolve("[Phone Number]"), contains("B"));
        assertThat(resolve("[phone number]"), contains("B"));

    }

    @Test
    public void formId() {
        givenRootForm("Contact Form", field("A", "Name"), field("B", "Phone Number"));

        assertThat(resolve("_class"), contains("Contact Form@formId"));

    }

    @Test(expected = AmbiguousSymbolException.class)
    public void ambiguousRootField() {
        givenRootForm("Contact Form", field("A", "Name"), field("B", "name"));

        // Should always be able to resolve by ID
        assertThat(resolve("A"), contains("A"));

        // If there are conflicting matches at the root level, then throw an exception
        resolve("Name");
    }

    @Test
    public void childMatch() {
        givenRootForm("Project Site",
                field("A", "Name"),
                referenceField("B", "Location",
                        formClass("LocationForm",
                                field("LA", "Name"),
                                field("LB", "Population"))));


        assertThat(resolve("Name"), contains("A"));
        assertThat(resolve("Location.Name"), contains("B>LA"));
        assertThat(resolve("Population"), contains("B>LB"));
    }

    @Test
    public void childMatchPoint() {
        givenRootForm("Project Site",
                field("A", "Name"),
                referenceField("B", "Location",
                        formClass("LocationForm",
                                field("LA", "Name"),
                                field("LB", "Population"),
                                pointField("P", "Point"))));


        assertThat(resolve("Name"), contains("A"));
        assertThat(resolve("Location.Name"), contains("B>LA"));
        assertThat(resolve("Location.Latitude"), contains("B>P.latitude"));
        assertThat(resolve("Location.Longitude"), contains("B>P.longitude"));

        assertThat(resolve("Latitude"), contains("B>P.latitude"));

    }


    @Test
    public void childUnionMatch() {
        givenRootForm("Site",
                field("A", "Name"),
                referenceField("B", "Location",
                        formClass("Village",
                                field("VA", "Name"),
                                field("VB", "Population")),
                        formClass("Health Center",
                                field("HA", "Name"))));
        prettyPrintTree();

        assertThat(resolve("Name"), contains("A"));
        assertThat(resolve("Location.Name"), Matchers.containsInAnyOrder("B>VA", "B>HA"));
        assertThat(resolve("Location.Population"), contains("B>VB"));
    }

    @Test
    public void descendantUnionResourceId() {
        given(formClass("Province", field("PN", "Name")));
        given(formClass("Territoire", field("TN", "Name"), referenceField("TP", "Province", "Province")));

        givenRootForm("Project Site",
                field("A", "Name"),
                referenceField("B", "Location", "Province", "Territoire"));

        prettyPrintTree();

        assertThat(resolve("Province._id"), contains("B>Province@id", "B>TP>Province@id"));
    }

    @Test
    public void descendantFormClass() {
        given(formClass("Province", field("PN", "Name")));
        given(formClass("Territoire", field("TN", "Name"), referenceField("TP", "Province", "Province")));
        givenRootForm("Project Site",
                field("SN", "Name"),
                referenceField("SL", "Location",
                        "Territoire",
                        "Province"));


        assertThat(resolve("Name"), contains("SN"));
        assertThat(resolve("Province.Name"), Matchers.containsInAnyOrder("SL>TP>PN", "SL>PN"));
    }

    @Test
    public void selfFormClass() {
        givenRootForm("Province", field("PN", "Name"));

        assertThat(resolve("Province.Name"), contains("PN"));
    }


    @Test
    public void embeddedField() {
        givenRootForm("village", field("VN", "name"), pointField("P", "location"));

        assertThat(resolve("P.latitude"), containsInAnyOrder("P.latitude"));
    }

    @Test
    public void enumField() {
        givenRootForm("distribution", multiEnum("KC", "Kit Contents", "Blankets", "Cookware", "Tent"));

        assertThat(resolve("[Kit Contents].Blankets"), Matchers.contains("KC.Blankets"));

    }


    private void prettyPrintTree() {
        FormTreePrettyPrinter prettyPrinter = new FormTreePrettyPrinter();
        prettyPrinter.printTree(tree());
    }


    private void givenRootForm(String label, FormField... fields) {
        if(rootFormClass != null) {
            throw new IllegalStateException("Root Form Class already set");
        }

        rootFormClass = new FormClass(ResourceId.valueOf(label));
        rootFormClass.setLabel(label);
        rootFormClass.getElements().addAll(Arrays.asList(fields));
    }


    private void given(FormClass formClass) {
        formClasses.put(formClass.getId(), formClass);
    }

    private FormField field(String id, String label) {
        FormField field = new FormField(ResourceId.valueOf(id));
        field.setLabel(label);
        field.setType(TextType.SIMPLE);

        return field;
    }

    private FormClass formClass(String id, FormField... fields) {
        FormClass formClass = new FormClass(ResourceId.valueOf(id));
        formClass.setLabel(id);
        for (FormField field : fields) {
            formClass.addElement(field);
        }
        return formClass;
    }

    private FormField referenceField(String id, String label, FormClass... formClasses) {
        for (FormClass formClass : formClasses) {
            given(formClass);
        }

        String rangeIds[] = new String[formClasses.length];
        for (int i = 0; i < formClasses.length; i++) {
            rangeIds[i] = formClasses[i].getId().asString();
        }

        return referenceField(id, label, rangeIds);
    }


    private FormField pointField(String id, String label) {
        FormField field = new FormField(ResourceId.valueOf(id));
        field.setLabel(label);
        field.setType(GeoPointType.INSTANCE);
        return field;
    }

    private FormField referenceField(String id, String label, String... formClasses) {
        List<ResourceId> range = new ArrayList<>();
        for (String formClass : formClasses) {
            range.add(ResourceId.valueOf(formClass));
        }
        ReferenceType type = new ReferenceType();
        type.setCardinality(Cardinality.SINGLE);
        type.setRange(range);

        FormField field = new FormField(ResourceId.valueOf(id));
        field.setLabel(label);
        field.setType(type);

        return field;
    }

    private FormField multiEnum(String id, String label, String... values) {
        List<EnumItem> enumValues = Lists.newArrayList();
        for (String value : values) {
            enumValues.add(new EnumItem(ResourceId.valueOf(value), value));
        }
        EnumType type = new EnumType(Cardinality.MULTIPLE, enumValues);

        FormField field = new FormField(ResourceId.valueOf(id));
        field.setLabel(label);
        field.setType(type);

        return field;
    }


    private Collection<String> resolve(String exprString) {

        FormTree tree = tree();

        symbolTable = new NodeMatcher(tree);

        FormulaNode expr = FormulaParser.parse(exprString);
        Collection<NodeMatch> matches;
        if(expr instanceof SymbolNode) {
            matches = symbolTable.resolveSymbol((SymbolNode) expr);
        } else if(expr instanceof CompoundExpr) {
            matches = symbolTable.resolveCompoundExpr((CompoundExpr) expr);
        } else {
            throw new IllegalArgumentException(exprString);
        }

        // Create a string that we can match against easily
        List<String> strings = Lists.newArrayList();
        for (NodeMatch match : matches) {
            strings.add(match.toDebugString());
        }

        System.out.println("Resolved " + exprString + " => " + strings);

        return strings;
    }

    private FormTree tree() {
        if(rootFormClass == null) {
            throw new IllegalStateException("Root FormClass is unset");
        }

        FormClassProvider provider = new FormClassProvider() {

            @Override
            public FormClass getFormClass(ResourceId formId) {
                if(rootFormClass.getId().equals(formId)) {
                    return rootFormClass;
                }
                FormClass formClass = formClasses.get(formId);
                if(formClass == null) {
                    throw new IllegalArgumentException(formId.toString());
                }
                return formClass;
            }
        };
        FormTreeBuilder builder = new FormTreeBuilder(provider);
        return builder.queryTree(rootFormClass.getId());
    }


}