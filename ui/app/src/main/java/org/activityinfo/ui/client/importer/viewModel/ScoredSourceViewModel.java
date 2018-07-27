package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.ui.client.importer.viewModel.fields.ColumnMatchMatrix;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnTarget;

import java.util.List;

public class ScoredSourceViewModel {
    private SourceViewModel viewModel;
    private ColumnMatchMatrix matchMatrix;

    public ScoredSourceViewModel(SourceViewModel viewModel, List<ColumnTarget> targets) {
        this.viewModel = viewModel;
        this.matchMatrix = new ColumnMatchMatrix(viewModel.getColumns(), targets);
    }

    public SourceViewModel getSource() {
        return viewModel;
    }

    public ColumnMatchMatrix getMatchMatrix() {
        return matchMatrix;
    }

    public List<SourceColumn> getColumns() {
        return viewModel.getColumns();
    }
}
