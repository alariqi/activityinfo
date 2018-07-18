package org.activityinfo.ui.client.importer.viewModel.fields;

import java.util.List;

public class BestColumnTargets {
    private final List<ScoredColumnTarget> nameMatches;
    private final List<ScoredColumnTarget> contentMatches;
    private final List<ScoredColumnTarget> other;

    public BestColumnTargets(List<ScoredColumnTarget> nameMatches, List<ScoredColumnTarget> contentMatches, List<ScoredColumnTarget> other) {
        this.nameMatches = nameMatches;
        this.contentMatches = contentMatches;
        this.other = other;
    }

    public List<ScoredColumnTarget> getNameMatches() {
        return nameMatches;
    }

    public List<ScoredColumnTarget> getContentMatches() {
        return contentMatches;
    }

    public List<ScoredColumnTarget> getOther() {
        return other;
    }
}
