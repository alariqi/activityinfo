package org.activityinfo.ui.client.importer.viewModel;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.activityinfo.json.Json;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.resource.RecordUpdate;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.ReferenceValue;
import org.activityinfo.model.type.primitive.TextValue;
import org.activityinfo.observable.Connection;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.importer.state.FieldMapping;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.state.ImportSource;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnTarget;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModel;
import org.activityinfo.ui.client.store.TestSetup;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class ImportViewModelTest {

    private static final int COLUMN_WIDTH = 20;

    private TestSetup testSetup = new TestSetup();
    private StatefulValue<ImportState> state;
    private ImportViewModel viewModel;
    private Connection<SourceViewModel> sourceView;
    private Connection<MappedSourceViewModel> mappedView;
    private Connection<ValidatedTable> validatedView;
    private Connection<ImportedTable> importedView;

    @Test
    public void qis() throws IOException {

        ResourceId formId = ResourceId.valueOf("a0000000001");

        loadDataSet("qis.json");
        startImport(formId);

        importResource("qis.csv");

        dumpTargets();
        dumpColumnMatrix();

        map("MEMBER_NO_ADULT_MALE", "NumAdultMale");
        map("MEMBER_NO_ADULT_FEMALE", "NumAdultFemale");
        map("_CREATION_DATE", "Start Date");
        map("_SUBMISSION_DATE", "End Date");
        map("district name", "District Name");
        map("upazila", "Upzilla Name");
        map("Partner", "Partner Name");

        dumpMappings();
        dumpColumnHeaders();

        assertMappingComplete();

        verifyImport("qis-expected.json");
    }

    @Test
    public void missingAdminLevel() throws IOException {
        ResourceId formId = ResourceId.valueOf("L0000000002");

        loadDataSet("qis-villages.json");
        startImport(formId);

        importResource("qis-villages.csv");

        dumpTargets();
        dumpColumnMatrix();
        dumpDerivedMappings();

        assertMapped("District", "District Name");
        assertMapped("Name", "Name");

        dumpValidation();


        RecordRef chittagongRef = new RecordRef(ResourceId.valueOf("E0000000002"), ResourceId.valueOf("z0000000002"));

        List<RecordUpdate> records = importRecords();
        assertThat(records, hasSize(1));
        assertThat(getField(records.get(0), "Name"), equalTo(TextValue.valueOf("Village 1")));
        assertThat(getField(records.get(0), "Administrative Unit"), equalTo(new ReferenceValue(chittagongRef)));
    }

    @Test
    @Ignore("wip")
    public void nfiActivity() throws IOException {

        ResourceId formId = ResourceId.valueOf("a0000000033");

        loadDataSet("nfi.json");
        startImport(formId);

        importResource("nfi.csv");

        dumpDerivedMappings();
    }

    @Test
    @Ignore("wip")
    public void camps() throws IOException {

        ResourceId formId = ResourceId.valueOf("L0000001451");

        loadDataSet("somalia.json");
        startImport(formId);

        importResource("somali-camps.csv");


        map("Region", "Region Name");
        map("Admin2", "District Name");
        map("Village Name", "Name");
        map("Pcode", "Alternate Name");
        map("Latitude", "Geographic coordinates - Latitude");
        map("Longitude", "Geographic coordinates - Longitude");

        assertMappingComplete();

        dumpDerivedMappings();

        verifyImport("somalia-expected.json");
    }

    @Test
    @Ignore("wip")
    public void schools() throws IOException {

        ResourceId formId = ResourceId.valueOf("L0000000002");

        loadDataSet("nfi.json");
        startImport(formId);

        importResource("schools.csv");

        dumpDerivedMappings();


    }



    /**
     * Loads a test dataset of forms and their records from a JSON file.
     */
    private void loadDataSet(String resourceName) throws IOException {
        testSetup.getCatalog().loadDataSet(Resources.getResource(ImportViewModel.class, resourceName));
    }

    /**
     * Starts the import process
     */
    private void startImport(ResourceId formId) {
        state = new StatefulValue<>(new ImportState(formId));
        viewModel = new ImportViewModel(
                testSetup.getFormStore(),
                testSetup.getFormTree(formId),
                state);

        sourceView = testSetup.connect(viewModel.getSource());
        mappedView = testSetup.connect(viewModel.getMappedSource());
        validatedView = testSetup.connect(viewModel.getValidatedTable());
        importedView = testSetup.connect(viewModel.getImportedTable());
    }

    private void importResource(String resourceName) throws IOException {
        URL resource = Resources.getResource(ImportViewModel.class, resourceName);
        String text = Resources.toString(resource, Charsets.UTF_8);
        Optional<ImportSource> source = Optional.of(new ImportSource(text));

        state.update(s -> s.withSource(source));
    }

    /**
     * Updates the field mapping set by matching the column with the given {@code columnLabel} to the
     * target with the given {@code targetLabel}
     *
     */
    private void map(String columnLabel, String targetLabel) {
        ColumnTarget target = findTargetByLabel(targetLabel);
        String columnId = findColumnIdByLabel(columnLabel);
        state.update(s -> s.updateMappings(m -> target.apply(m, columnId)));
    }

    private void dumpDerivedMappings() {
        dumpHeader("Derived Mappings");

        FieldMappingSet derivedMappings = mappedView.assertLoaded().getFieldMappingSet();
        for (FieldMapping derivedMapping : derivedMappings) {
            System.out.println(findColumnLabelById(
                    derivedMapping.getColumnId()) + " => " +
                    findTargetLabel(derivedMapping));
        }
    }

    private void dumpHeader(final String header) {
        System.out.println();
        System.out.println("==== " + header + " ====");
    }


    private void assertMapped(String columnLabel, String targetLabel) {

        ColumnTarget target = findTargetByLabel(targetLabel);
        String columnId = findColumnIdByLabel(columnLabel);

        FieldMappingSet fieldMappingSet = mappedView.assertLoaded().getFieldMappingSet();
        if (!target.isApplied(columnId, fieldMappingSet)) {
            throw new AssertionError("Column " + columnLabel + " is not mapped to " + targetLabel);
        }
    }

    private void assertMappingComplete() {
        if(!mappedView.assertLoaded().isComplete()) {
            throw new AssertionError("Mapping is not complete: " +
                    mappedView.assertLoaded()
                            .getMissingRequiredFields()
                            .stream()
                            .map(f -> f.getField().getLabel())
                            .collect(Collectors.joining(", ")));
        }
    }

    private ColumnTarget findTargetByLabel(String label) {
        for (FieldViewModel field : viewModel.getFields()) {
            for (ColumnTarget columnTarget : field.getTargets()) {
                if(columnTarget.getLabel().equals(label)) {
                    return columnTarget;
                }
            }
        }
        throw new AssertionError("No column target with label '" + label + "'");
    }


    private String findTargetLabel(FieldMapping mapping) {
        for (FieldViewModel field : viewModel.getFields()) {
            if(field.getFieldName().equals(mapping.getFieldName())) {
                for (ColumnTarget target : field.getTargets()) {
                    if (target.getRole().equals(mapping.getRole())) {
                        return target.getLabel();
                    }
                }
            }
        }
        throw new RuntimeException("Cannot find target for " + mapping);
    }

    private String findColumnIdByLabel(String columnLabel) {
        for (SourceColumn column : sourceView.assertLoaded().getColumns()) {
            if(column.getLabel().equals(columnLabel)) {
                return column.getId();
            }
        }
        throw new AssertionError("No column with label '" + columnLabel + "'");
    }

    private String findColumnLabelById(String columnId) {
        return sourceView.assertLoaded().getColumns().stream()
                .filter(c -> c.getId().equals(columnId))
                .map(c -> c.getLabel())
                .findAny()
                .orElse(columnId);
    }


    private void dumpValidation() {
        dumpHeader("Validation Table");

        ValidatedTable validatedTable = validatedView.assertLoaded();

        printRow(validatedTable.getColumns().stream().map(c -> c.getStatus().name()));
        printRow(validatedTable.getColumns().stream().map(c -> c.getColumn().getLabel()));
        printRow(validatedTable.getColumns().stream().map(c -> Strings.repeat("-", COLUMN_WIDTH)));

        for (int i = 0; i < validatedTable.getNumRows(); i++) {
            int rowIndex = i;
            printRow(validatedTable.getColumns().stream().map(c -> {
                return validationSymbol(c.getValidation().getRowStatus(rowIndex)) + " " +
                        c.getColumnView().getString(rowIndex);
            }));
        }

    }

    private String validationSymbol(int status) {
        switch (status) {
            case Validation.VALID:
                return "✓";
            case Validation.VALIDATING:
                return "⌛";
            case Validation.INVALID:
                return "✗";
        }
        return "?";
    }

    private void printRow(Stream<?> cells) {
        StringBuilder line = new StringBuilder();
        Iterator<?> it = cells.iterator();
        while(it.hasNext()) {
            line.append("| ");
            line.append(cell(it.next()));
            line.append(" ");
        }
        line.append("|");
        System.out.println(line.toString());
    }

    private String cell(Object o) {
        String text;
        if(o == null) {
            text = "";
        } else {
            text = o.toString();
        }
        if(text.length() <= COLUMN_WIDTH) {
            return Strings.padEnd(text, COLUMN_WIDTH, ' ');
        } else {
            return text.substring(0, COLUMN_WIDTH - 3) + "...";
        }
    }



    private void dumpColumnMatrix() throws IOException {
        File tempFile = File.createTempFile("matrix", ".csv");
        Files.asCharSink(tempFile, Charsets.UTF_8).write(mappedView.assertLoaded().getColumnMatrix().toCsv(true));

        System.out.println("Wrote column matrix to " + tempFile.getAbsolutePath());
    }


    private void dumpMappings() {
        System.out.println("==== Field Mappings ====");
        for (FieldMapping fieldMapping : mappedView.assertLoaded().getFieldMappingSet()) {
            System.out.println(fieldMapping);
        }
    }

    private void dumpTargets() {
        System.out.println("==== Column Targets ====");

        for (FieldViewModel field : viewModel.getFields()) {
            System.out.println(field.getField().getLabel() + ": " + field.getTargets());
        }
    }


    private void dumpColumnHeaders() {
        System.out.println("=== Match Table Headers === ");
        for (MappedSourceColumn column : mappedView.assertLoaded().getColumns()) {
            System.out.println(column.getStatusLabel() + " / " + column.getLabel());
        }
    }

    private List<RecordUpdate> importRecords() {
        return Lists.newArrayList(importedView.assertLoaded().getRecords());

    }

    private FieldValue getField(RecordUpdate record, String fieldLabel) {
        List<FormField> fields = viewModel.getFormTree().getRootFormClass().getFields();
        for (FormField field : fields) {
            if(field.getLabel().equals(fieldLabel)) {
                return field.getType().parseJsonValue(record.getFields().get(field.getName()));
            }
        }
        throw new AssertionError("No such field '" + fieldLabel + "', root fields = " +
            fields.stream().map(f -> "'" + f.getLabel() + "'").collect(Collectors.joining(", ")));
    }

    private void verifyImport(String resourceName) throws IOException {
        URL testResource = Resources.getResource(ImportViewModelTest.class, resourceName);
        String json = Resources.toString(testResource, Charsets.UTF_8);
        JsonValue array = Json.parse(json);

        List<RecordUpdate> records = importRecords();

        List<FormField> fields = viewModel.getFormTree().getRootFormClass().getFields();

        if(records.size() != array.length()) {
            throw new AssertionError("Expected " + array.length() + " records to be imported, found " + records.size());
        }

        int mismatchCount = 0;

        for (int i = 0; i < array.length(); i++) {
            JsonValue expectedFields = array.get(i).get("fields");
            JsonValue actualFields = records.get(i).getFields();

            for (String fieldName : expectedFields.keys()) {
                JsonValue expectedValue = expectedFields.get(fieldName);
                JsonValue actualValue = actualFields.get(fieldName);
                if(!Objects.equals(expectedValue, actualValue)) {
                    System.out.println("In row #" + i + ": " + actualValue.toJson() + " != " + expectedValue.toJson());
                    mismatchCount++;
                }
            }
        }
        if(mismatchCount > 0) {
            throw new AssertionError("Imported data had " + mismatchCount + " mismatch(es)");
        }
    }

    private void writeImportResults() throws IOException {
        JsonValue array = Json.createArray();
        Iterator<RecordUpdate> it = importedView.assertLoaded().getRecords();
        while(it.hasNext()) {
            array.add(Json.toJson(it.next()));
        }
        Files.asCharSink(new File("/tmp/tmp.json"), Charsets.UTF_8).write(Json.stringify(array, 2));
    }

}