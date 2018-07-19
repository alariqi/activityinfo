package org.activityinfo.ui.client.importer.viewModel.fields;

import java.util.List;

public class BestColumnTargets {
    private final List<ScoredColumnTarget> nameMatches;
    private final List<ScoredColumnTarget> other;

    public BestColumnTargets(List<ScoredColumnTarget> best, List<ScoredColumnTarget> other) {
        this.nameMatches = best;
        this.other = other;
    }

    public List<ScoredColumnTarget> getNameMatches() {
        return nameMatches;
    }

    public List<ScoredColumnTarget> getOther() {
        return other;
    }
}
