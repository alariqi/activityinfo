package org.activityinfo.ui.client.importer.state;

import org.activityinfo.model.resource.ResourceId;

import java.util.Optional;
import java.util.function.Function;

public class ImportState {



    public enum ImportStep {
        CHOOSE_SOURCE,
        MATCH_COLUMNS
    }

    private ResourceId formId;
    private Optional<ImportSource> source = Optional.empty();
    private ImportStep step = ImportStep.CHOOSE_SOURCE;
    private String selectedColumnId = "";
    private FieldMappingSet fieldMappings = new FieldMappingSet();

    private ImportState(ImportState from) {
        this.formId = from.formId;
        this.source = from.source;
        this.step = from.step;
        this.selectedColumnId = from.selectedColumnId;
    }

    public ImportState(ResourceId formId) {
        this.formId = formId;
    }

    public ResourceId getFormId() {
        return formId;
    }

    public Optional<ImportSource> getSource() {
        return source;
    }

    public ImportStep getStep() {
        return step;
    }

    public String getSelectedColumnId() {
        return selectedColumnId;
    }

    public boolean isEmpty() {
        return !source.isPresent();
    }

    public FieldMappingSet getMappings() {
        return fieldMappings;
    }

    public ImportState withSource(Optional<ImportSource> source) {
        ImportState copy = new ImportState(this);
        copy.source = source;
        return copy;
    }

    public ImportState withStep(ImportStep step) {
        if(this.step == step) {
            return this;
        }
        if(step == ImportStep.MATCH_COLUMNS && !source.isPresent()) {
            return this;
        }

        ImportState copy = new ImportState(this);
        copy.step = step;
        return copy;
    }


    public ImportState selectColumn(String columnId) {
        if(this.selectedColumnId.equals(columnId)) {
            return this;
        }
        ImportState copy = new ImportState(this);
        copy.selectedColumnId = columnId;
        return copy;
    }


    public ImportState updateMappings(Function<FieldMappingSet, FieldMappingSet> function) {
        FieldMappingSet updated = function.apply(this.fieldMappings);
        if(updated == fieldMappings) {
            return this;
        }
        ImportState copy = new ImportState(this);
        copy.fieldMappings = updated;
        return copy;
    }

}
