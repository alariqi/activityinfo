package org.activityinfo.ui.client.importer.viewModel;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Connection;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.importer.state.FieldMapping;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.state.ImportSource;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnMatchMatrix;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnTarget;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModel;
import org.activityinfo.ui.client.store.TestSetup;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class ImportViewModelTest {

    private TestSetup testSetup = new TestSetup();
    private StatefulValue<ImportState> state;
    private ImportViewModel viewModel;
    private Connection<SourceViewModel> sourceView;
    private Connection<FieldMappingSet> mappingView;
    private Connection<ColumnMatchMatrix> columnMatrixView;


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
        columnMatrixView = testSetup.connect(viewModel.getColumnMatchMatrix());
        mappingView = testSetup.connect(viewModel.getMappings());
    }

    private void importResource(String resourceName) throws IOException {
        URL resource = Resources.getResource(ImportViewModel.class, resourceName);
        String text = Resources.toString(resource, Charsets.UTF_8);
        Optional<ImportSource> source = Optional.of(new ImportSource(text));

        state.update(s -> s.withSource(source));
    }

    private String field(String fieldLabel) {
        for (FormTree.Node node : viewModel.getFormTree().getRootFields()) {
            if(node.getField().getLabel().equals(fieldLabel)) {
                return node.getFieldId().asString();
            }
        }
        throw new AssertionError("No field with label " + fieldLabel);
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

    private String findColumnIdByLabel(String columnLabel) {
        for (SourceColumn column : sourceView.assertLoaded().getColumns()) {
            if(column.getLabel().equals(columnLabel)) {
                return column.getId();
            }
        }
        throw new AssertionError("No column with label '" + columnLabel + "'");
    }




    private void dumpColumnMatrix() throws IOException {
        File tempFile = File.createTempFile("matrix", ".csv");
        Files.asCharSink(tempFile, Charsets.UTF_8).write(columnMatrixView.assertLoaded().toCsv());

        System.out.println("Wrote column matrix to " + tempFile.getAbsolutePath());
    }


    private void dumpMappings() {
        System.out.println("==== Field Mappings ====");
        for (FieldMapping fieldMapping : mappingView.assertLoaded().getMappings()) {
            System.out.println(fieldMapping);
        }
    }

    private void dumpTargets() {
        System.out.println("==== Column Targets ====");

        for (FieldViewModel field : viewModel.getFields()) {
            System.out.println(field.getField().getLabel() + ": " + field.getTargets());
        }
    }

}