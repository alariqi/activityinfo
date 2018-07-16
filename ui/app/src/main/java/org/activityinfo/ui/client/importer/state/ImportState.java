package org.activityinfo.ui.client.importer.state;

import org.activityinfo.model.resource.ResourceId;

import java.util.Optional;

public class ImportState {


    public enum ImportStep {
        CHOOSE_SOURCE,
        MATCH_COLUMNS
    }

    private ResourceId formId;
    private Optional<ImportSource> source = Optional.empty();
    private ImportStep step = ImportStep.CHOOSE_SOURCE;

    private ImportState(ImportState from) {
        this.formId = from.formId;
        this.source = from.source;
        this.step = from.step;
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

    public boolean isEmpty() {
        return !source.isPresent();
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

}
