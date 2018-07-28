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
    public static final double PROBABLE_THRESHOLD = 0.5;

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
                double nameScore = SCORER.score(column.getLabel(), target.getLabel());
                nameScores[scoreIndex] = nameScore;
                contentScores[scoreIndex] = contentScore;
                scoreIndex++;
            }
        }

        LOGGER.info("Match matrix\n" + toCsv(true));
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

    public ColumnTarget getTarget(int index) {
        return targets.get(index);
    }

    public SourceColumn getSourceColumn(int index) {
        return columns.get(index);
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
        return nameScores[(targetIndex * numColumns) + columnIndex];
    }

    public double getContentScore(int columnIndex, int targetIndex) {
        return contentScores[(targetIndex * numColumns) + columnIndex];
    }

    /**
     * Dumps this score matrix to a CSV string that can be used for debugging.
     * @param scoreByName true to score by name, otherwise by content
     */
    public String toCsv(boolean scoreByName) {
        StringBuilder csv = new StringBuilder();
        for (SourceColumn column : columns) {
            csv.append("\t").append(column.getLabel());
        }
        csv.append("\n");
        for (int i = 0; i < numTargets; i++) {
            csv.append(targets.get(i).getLabel());
            for (int j = 0; j < numColumns; ++j) {
                double score = scoreByName ? getNameScore(j, i) : getContentScore(j, i);
                csv.append("\t").append(score);
            }
            csv.append("\n");
        }
        return csv.toString();
    }

    public BestColumnTargets findBestTargets(String columnId) {
        List<ScoredColumnTarget> nameMatches = new ArrayList<>();
        List<ScoredColumnTarget> other = new ArrayList<>();

        int columnIndex = findColumnIndexFromId(columnId);
        if(columnIndex != -1) {
            for (int i = 0; i < numTargets; i++) {
                ColumnTarget target = targets.get(i);
                double nameScore = getNameScore(columnIndex, i);
                double contentScore = getContentScore(columnIndex, i);
                if(nameScore > PROBABLE_THRESHOLD) {
                    nameMatches.add(new ScoredColumnTarget(target, nameScore, contentScore));
                } else if(contentScore > 0) {
                    other.add(new ScoredColumnTarget(target, nameScore, contentScore));
                }
            }
        }
        nameMatches.sort(Ordering.natural());
        other.sort(Ordering.natural());

        return new BestColumnTargets(nameMatches, other);
    }

    /**
     * Finds the best target for a given columnId, given the filter {@code unmatchedTargets}
     * @return the index of the best target match, or -1 if there is no match
     */
    public int findBestTarget(String columnId, boolean[] unmatchedTargets) {
        int columnIndex = findColumnIndexFromId(columnId);
        if(columnIndex < 0) {
            return -1;
        }

        int bestTargetIndex = -1;
        double bestScore = Double.MIN_VALUE;

        for (int i = 0; i < numTargets; i++) {
            if(unmatchedTargets[i]) {
                double score = getNameScore(columnIndex, i);
                if(score > bestScore && score > PROBABLE_THRESHOLD) {
                    bestTargetIndex = i;
                    bestScore = score;
                }
            }
        }

        return bestTargetIndex;
    }
}
