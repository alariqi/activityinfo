package org.activityinfo.ui.client.importer.viewModel.fields;

public class ScoredColumnTarget implements Comparable<ScoredColumnTarget> {
    private ColumnTarget target;
    private double score;

    public ScoredColumnTarget(ColumnTarget target, double score) {
        this.target = target;
        this.score = score;
    }

    @Override
    public int compareTo(ScoredColumnTarget o) {
        return Double.compare(this.score, o.score);
    }
}
