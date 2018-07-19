package org.activityinfo.ui.client.importer.viewModel.fields;

public class ScoredColumnTarget implements Comparable<ScoredColumnTarget> {
    private final ColumnTarget target;
    private final double score;
    private final double contentScore;

    public ScoredColumnTarget(ColumnTarget target, double nameScore, double contentScore) {
        this.target = target;
        this.score = nameScore;
        this.contentScore = contentScore;
    }

    public ColumnTarget getTarget() {
        return target;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(ScoredColumnTarget o) {
        return -Double.compare(this.score, o.score);
    }
}
