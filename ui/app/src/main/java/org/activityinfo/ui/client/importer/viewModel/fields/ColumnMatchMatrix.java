package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.io.match.names.LatinPlaceNameScorer;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import java.util.List;
import java.util.logging.Logger;

public class ColumnMatchMatrix {

    private static final Logger LOGGER = Logger.getLogger(ColumnMatchMatrix.class.getName());

    private static final LatinPlaceNameScorer SCORER = new LatinPlaceNameScorer();

    private List<SourceColumn> columns;
    private List<ColumnTarget> targets;
    private final int numColumns;
    private final int numTargets;
    private final double[] score;

    public ColumnMatchMatrix(List<SourceColumn> columns, List<ColumnTarget> targets) {
        this.columns = columns;
        this.targets = targets;
        numColumns = columns.size();
        numTargets = targets.size();
        score = new double[numColumns * numTargets];
        int scoreIndex = 0;

        for (ColumnTarget target : targets) {
            for (SourceColumn column : columns) {
                score[scoreIndex++] = score(column, target);
            }
        }
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getNumTargets() {
        return numTargets;
    }

    public double getScore(int columnIndex, int targetIndex) {
        return score[(columnIndex * numTargets) + targetIndex];
    }

    private double score(SourceColumn column, ColumnTarget columnTarget) {
        double contentScore = columnTarget.scoreContent(column);
        if(contentScore == 0) {
            return 0;
        }
        return SCORER.score(column.getLabel(), columnTarget.getLabel()) * contentScore;
    }

    public String toCsv() {
        StringBuilder csv = new StringBuilder();
        for (SourceColumn column : columns) {
            csv.append("\t").append(column.getLabel());
        }
        csv.append("\n");
        for (int i = 0; i < numTargets; i++) {
            csv.append(targets.get(i).getLabel());
            for (int j = 0; j < numColumns; ++j) {
                csv.append("\t").append(getScore(j, i));
            }
            csv.append("\n");
        }
        return csv.toString();
    }
}
