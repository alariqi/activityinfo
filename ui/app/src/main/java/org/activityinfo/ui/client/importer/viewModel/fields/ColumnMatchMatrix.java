package org.activityinfo.ui.client.importer.viewModel.fields;

import com.google.common.collect.Ordering;
import org.activityinfo.io.match.names.LatinPlaceNameScorer;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ColumnMatchMatrix {

    private static final Logger LOGGER = Logger.getLogger(ColumnMatchMatrix.class.getName());

    private static final LatinPlaceNameScorer SCORER = new LatinPlaceNameScorer();

    private final List<SourceColumn> columns;
    private final List<ColumnTarget> targets;
    private final int numColumns;
    private final int numTargets;
    private final double[] nameScores;
    private final double[] contentScores;

    public ColumnMatchMatrix(List<SourceColumn> columns, List<ColumnTarget> targets) {
        this.columns = columns;
        this.targets = targets;
        numColumns = columns.size();
        numTargets = targets.size();
        nameScores = new double[numColumns * numTargets];
        contentScores = new double[numColumns * numTargets];
        int scoreIndex = 0;

        for (ColumnTarget target : targets) {
            for (SourceColumn column : columns) {

                // We have two, independent, ways of matching columns to targets:
                // First, based on their headings/labels, and second, based on the imported
                // content. For example, a "Date" column might match "Start Date", but if the "Date" column
                // contains only numbers, then it cannot be a good match for a date field, regardless of the name.

                double contentScore = target.scoreContent(column);
                double nameScore;
                if(contentScore > 0) {
                    nameScore = SCORER.score(column.getLabel(), target.getLabel());
                } else {
                    nameScore = 0;
                }
                nameScores[scoreIndex] = nameScore;
                contentScores[scoreIndex] = contentScore;
                scoreIndex++;
            }
        }
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getNumTargets() {
        return numTargets;
    }

    public List<SourceColumn> getColumns() {
        return columns;
    }

    public List<ColumnTarget> getTargets() {
        return targets;
    }

    public int findColumnIndexFromId(String columnId) {
        for (int i = 0; i < columns.size(); i++) {
            if(columns.get(i).getId().equals(columnId)) {
                return i;
            }
        }
        return -1;
    }

    public double getNameScore(int columnIndex, int targetIndex) {
        return nameScores[(columnIndex * numTargets) + targetIndex];
    }

    public double getContentScore(int columnIndex, int targetIndex) {
        return contentScores[(columnIndex * numTargets) + targetIndex];
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
                csv.append("\t").append(getNameScore(j, i));
            }
            csv.append("\n");
        }
        return csv.toString();
    }

    public BestColumnTargets findBestTargets(String columnId) {
        List<ScoredColumnTarget> nameMatches = new ArrayList<>();
        List<ScoredColumnTarget> contentMatches = new ArrayList<>();
        List<ScoredColumnTarget> other = new ArrayList<>();

        int columnIndex = findColumnIndexFromId(columnId);
        if(columnIndex != -1) {
            for (int i = 0; i < numTargets; i++) {
                ColumnTarget target = targets.get(i);
                double nameScore = getNameScore(columnIndex, i);
                if(nameScore > 0.5) {
                    nameMatches.add(new ScoredColumnTarget(target, nameScore));
                } else {
                    double contentScore = getContentScore(columnIndex, i);
                    if(contentScore > 0.5) {
                        contentMatches.add(new ScoredColumnTarget(target, contentScore));
                    } else {
                        other.add(new ScoredColumnTarget(target, nameScore));
                    }
                }
            }
        }
        nameMatches.sort(Ordering.natural());
        contentMatches.sort(Ordering.natural());

        return new BestColumnTargets(nameMatches, contentMatches, other);
    }
}
