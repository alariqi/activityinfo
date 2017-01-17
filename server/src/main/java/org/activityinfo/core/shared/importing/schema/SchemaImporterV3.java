package org.activityinfo.core.shared.importing.schema;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.core.client.ResourceLocator;
import org.activityinfo.core.shared.importing.source.SourceColumn;
import org.activityinfo.core.shared.importing.source.SourceRow;
import org.activityinfo.core.shared.importing.source.SourceTable;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.form.SubFormKind;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.legacy.KeyGenerator;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.*;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.expr.CalculatedFieldType;
import org.activityinfo.model.type.number.QuantityType;
import org.activityinfo.model.type.subform.SubFormReferenceType;
import org.activityinfo.promise.Promise;
import org.activityinfo.promise.PromiseExecutionOperation;
import org.activityinfo.promise.PromisesExecutionGuard;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Imports a V3 schema CSV exported by {@link SchemaCsvWriterV3}
 */
public class SchemaImporterV3 extends SchemaImporter {


    private class EnumBuilder {
        private FormField formField;
        private Cardinality cardinality;
        private List<EnumItem> items = new ArrayList<>();

        public EnumBuilder(FormField field, Cardinality cardinality) {
            formField = field;
            this.cardinality = cardinality;
        }
    }

    private final KeyGenerator keyGenerator = new KeyGenerator();

    // columns
    private Column formName;
    private Column formFieldType;
    private Column fieldName;
    private Column fieldDescription;
    private Column fieldUnits;
    private Column fieldRequired;
    private Column choiceLabel;
    private Column fieldCode;
    private Column fieldExpression;
    private Column subForm;
    private Column subFormType;
    private Column references;

    private Map<String, FormClass> formMap = new HashMap<>();
    private Map<String, FormClass> subFormMap = new HashMap<>();
    private Map<String, EnumBuilder> enumMap = new HashMap<>();
    private int databaseId;
    private ResourceLocator locator;


    public SchemaImporterV3(int databaseId, ResourceLocator locator, WarningTemplates templates) {
        super(templates);
        this.databaseId = databaseId;
        this.locator = locator;
    }


    public SchemaImporterV3(int databaseId, ResourceLocator locator) {
        this(databaseId, locator, GWT.<WarningTemplates>create(WarningTemplates.class));
    }

    public boolean accept(SourceTable table) {
        for (SourceColumn sourceColumn : table.getColumns()) {
            if(sourceColumn.getHeader().equalsIgnoreCase("FormName")) {
                return true;
            }
        }
        return false;
    }

    private Column findColumn(SchemaCsvWriterV3.Column column) {
        return findColumn(column.getHeader(), null, Integer.MAX_VALUE);
    }

    private Column findColumn(SchemaCsvWriterV3.Column column, String defaultValue) {
        return findColumn(column.getHeader(), defaultValue, Integer.MAX_VALUE);
    }

    protected void findColumns() {
        formName = findColumn(SchemaCsvWriterV3.Column.FORM_NAME);
        formFieldType = findColumn(SchemaCsvWriterV3.Column.FIELD_TYPE);
        fieldName = findColumn(SchemaCsvWriterV3.Column.FIELD_NAME);
        fieldCode = findColumn(SchemaCsvWriterV3.Column.FIELD_CODE, "");
        fieldDescription = findColumn(SchemaCsvWriterV3.Column.FIELD_DESCRIPTION, "");
        fieldUnits = findColumn(SchemaCsvWriterV3.Column.FIELD_UNITS, "units");
        fieldRequired = findColumn(SchemaCsvWriterV3.Column.FIELD_REQUIRED, "false");
        fieldExpression = findColumn(SchemaCsvWriterV3.Column.FIELD_EXPR, "");
        choiceLabel = findColumn(SchemaCsvWriterV3.Column.ENUM_LABEL, "");
        subForm = findColumn(SchemaCsvWriterV3.Column.SUBFORM, "");
        subFormType = findColumn(SchemaCsvWriterV3.Column.SUBFORM_TYPE, SubFormKind.REPEATING.name());
        references = findColumn(SchemaCsvWriterV3.Column.FIELD_RANGE, "");
    }

    public boolean processRows() {
        formMap.clear();
        subFormMap.clear();
        enumMap.clear();

        fatalError = false;
        for (SourceRow row : source.getRows()) {

            try {

                FormClass parentFormClass = getFormClass(row);
                FormClass formClass = getSubFormClass(parentFormClass, row);

                String type = formFieldType.get(row);
                if (isEnum(type)) {
                    addChoice(formClass, row);
                } else {
                    FieldType fieldType = parseFieldType(row);
                    FormField newField = addField(formClass, fieldType.getTypeClass(), row);
                    newField.setType(fieldType);
                }
            } catch (UnableToParseRowException e) {
                warnings.add(SafeHtmlUtils.fromString(e.getMessage()));
                fatalError = true;
            }
        }

        for (EnumBuilder enumBuilder : enumMap.values()) {
            enumBuilder.formField.setType(new EnumType(enumBuilder.cardinality, enumBuilder.items));
        }

        return !fatalError;
    }

    public List<FormClass> toSave() {
        List<FormClass> formClasses = new ArrayList<>();
        formClasses.addAll(formMap.values());
        formClasses.addAll(subFormMap.values());
        return formClasses;
    }

    private void addChoice(FormClass formClass, SourceRow row) {

        String fieldLabel = fieldName.get(row);
        String fieldKey = formClass.getId() + fieldLabel;

        EnumBuilder enumField = enumMap.get(fieldKey);
        if(enumField == null) {
            FormField newField = addField(formClass, EnumType.TYPE_CLASS, row);
            enumField = new EnumBuilder(newField, parseCardinality(row));
            enumMap.put(fieldKey, enumField);
        }

        enumField.items.add(new EnumItem(EnumItem.generateId(), choiceLabel.get(row)));
    }

    private FormField addField(FormClass formClass, FieldTypeClass typeClass, SourceRow row) {
        FormField field = new FormField(ResourceId.generateFieldId(typeClass));
        field.setLabel(fieldName.getOrThrow(row));
        field.setCode(fieldCode.get(row));
        field.setDescription(fieldDescription.get(row));
        field.setRequired(isTruthy(fieldRequired.get(row)));
        field.setVisible(true);

        formClass.addElement(field);

        return field;
    }

    private Cardinality parseCardinality(SourceRow row) {
        String type = formFieldType.get(row);
        if(type.equalsIgnoreCase(SchemaCsvWriterV3.MULTIPLE_SELECT)) {
            return Cardinality.MULTIPLE;
        } else {
            return Cardinality.SINGLE;
        }
    }

    private boolean isEnum(String type) {
        return SchemaCsvWriterV3.SINGLE_SELECT.equalsIgnoreCase(type) ||
                SchemaCsvWriterV3.MULTIPLE_SELECT.equalsIgnoreCase(type);
    }

    private FieldType parseFieldType(SourceRow row) {
        FieldTypeClass fieldTypeClass = parseFieldTypeClass(row);
        if(fieldTypeClass == QuantityType.TYPE_CLASS) {
            return new QuantityType(fieldUnits.get(row));
        } else if(fieldTypeClass == ReferenceType.TYPE_CLASS) {
            return new ReferenceType()
                    .setCardinality(Cardinality.SINGLE)
                    .setRange(parseRange(references.get(row)));

        } else if(fieldTypeClass == CalculatedFieldType.TYPE_CLASS) {
            return new CalculatedFieldType(fieldExpression.get(row));

        } else {
            return fieldTypeClass.createType();
        }
    }

    private Set<ResourceId> parseRange(String reference) {
        if(Strings.isNullOrEmpty(reference)) {
            throw new UnableToParseRowException(
                    I18N.MESSAGES.referenceFieldRequiresRange("REFERENCE",
                            SchemaCsvWriterV3.Column.FIELD_RANGE.getHeader()));
        }
        return Collections.singleton(ResourceId.valueOf(reference));
    }

    private FieldTypeClass parseFieldTypeClass(SourceRow row) {
        String type = formFieldType.get(row);
        for (FieldTypeClass fieldTypeClass : TypeRegistry.get().getTypeClasses()) {
            if(fieldTypeClass.getId().equalsIgnoreCase(type)) {
                return fieldTypeClass;
            }
        }
        return QuantityType.TYPE_CLASS;
    }

    private FormClass getFormClass(SourceRow row) {
        String name = formName.get(row);

        FormClass formClass = formMap.get(name);
        if (formClass == null) {
            formClass = new FormClass(CuidAdapter.activityFormClass(keyGenerator.generateInt()));
            formClass.setDatabaseId(databaseId);
            formClass.setLabel(name);

            formMap.put(name, formClass);
        }
        return formClass;
    }

    private FormClass getSubFormClass(FormClass masterForm, SourceRow row) {
        String subFormName = subForm.get(row);
        if(subFormName == null) {
            return masterForm;
        }

        FormClass subFormClass = subFormMap.get(subFormName);
        if(subFormClass == null) {
            subFormClass = new FormClass(ResourceId.generateId());
            subFormClass.setSubFormKind(parseSubFormKind(subFormType.get(row)));
            subFormClass.setParentFormId(masterForm.getId());
            subFormClass.setLabel(subFormName);
            subFormClass.setDatabaseId(databaseId);
            subFormMap.put(subFormName, subFormClass);

            FormField subFormField = new FormField(ResourceId.generateFieldId(SubFormReferenceType.TYPE_CLASS));
            subFormField.setLabel(subFormName);
            subFormField.setType(new SubFormReferenceType(subFormClass.getId()));
            subFormField.setVisible(true);
            masterForm.addElement(subFormField);
        }

        return subFormClass;
    }

    private SubFormKind parseSubFormKind(String kind) {
        for (SubFormKind subFormKind : SubFormKind.values()) {
            if(subFormKind.name().equalsIgnoreCase(kind)) {
                return subFormKind;
            }
        }
        return SubFormKind.REPEATING;
    }

    @Override
    public void persist(final AsyncCallback<Void> callback) {

        List<PromiseExecutionOperation> operations = new ArrayList<>();
        for (final FormClass formClass : toSave()) {
            operations.add(new PromiseExecutionOperation() {
                @Nullable
                @Override
                public Promise<Void> apply(@Nullable Void aVoid) {
                    return locator.persist(formClass);
                }
            });
        }

        PromisesExecutionGuard.newInstance().executeSerially(operations).then(callback);
    }
}