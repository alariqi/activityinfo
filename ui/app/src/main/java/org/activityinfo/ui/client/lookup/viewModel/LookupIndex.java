package org.activityinfo.ui.client.lookup.viewModel;

import org.activityinfo.io.match.names.LatinPlaceNameScorer;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Build a tree index to match imported values to a reference map
 *
 */
public class LookupIndex {

    private static final double SCORE_THRESHOLD = 0.5;

    public class Node {

        private Node parent;

        private String key;

        /**
         * Maps exact key names to child nodes or to record id
         */
        private Map<String, Integer> keyMap = new HashMap<>();
        private List<String> keys = new ArrayList<>();

        private List<Node> children;
        private List<String> recordIds;

        private Node getOrInsertChild(String keyString) {
            Integer childIndex = keyMap.get(keyString);
            if(childIndex == null) {
                if(children == null) {
                    children = new ArrayList<>();
                }
                childIndex = children.size();
                keyMap.put(keyString, childIndex);
                keys.add(keyString);

                Node child = new Node();
                child.parent = this;
                child.key = keyString;
                children.add(child);

                return child;
            } else {
                return children.get(childIndex);
            }
        }


        private void addKey(String leafKeyString, String id) {
            if(recordIds == null) {
                recordIds = new ArrayList<>();
            }
            int newKeyIndex = recordIds.size();
            keyMap.put(leafKeyString, newKeyIndex);
            keys.add(leafKeyString);
            recordIds.add(id);
        }

        private Node findChild(String keyString) {
            int matchIndex = matchKey(keyString);
            if(matchIndex < 0) {
                return null;
            } else {
                return children.get(matchIndex);
            }
        }

        private String findRecord(String keyString) {
            int matchIndex = matchKey(keyString);
            if(matchIndex < 0) {
                return null;
            } else {
                return recordIds.get(matchIndex);
            }
        }

        private int matchKey(String keyString) {
            Integer index = keyMap.get(keyString);
            if (index != null) {
                return index;
            }

            // Try fuzzy matching
            int bestIndex = -1;
            double bestScore = 0;
            for (int i = 0; i < keys.size(); i++) {
                double score = scorer.score(keyString, keys.get(i));
                if(score > SCORE_THRESHOLD && score > bestScore) {
                    bestIndex = i;
                    bestScore = score;
                }
            }

            // Cache result
            keyMap.put(keyString, bestIndex);

            return bestIndex;
        }
    }

    private final ResourceId formId;
    private final ColumnView idColumn;
    private final List<ColumnView> keys;
    private final ColumnView leafKey;

    private final LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();

    private Node root;


    public LookupIndex(ResourceId formId, ColumnView idColumn, List<ColumnView> keys) {
        this.formId = formId;
        this.idColumn = idColumn;
        this.keys = keys;
        this.leafKey = keys.get(keys.size() - 1);
        this.root = new Node();

        for (int i = 0; i < idColumn.numRows(); i++) {
            insertRow(i);
        }
    }

    public ResourceId getFormId() {
        return formId;
    }

    private void insertRow(int rowIndex) {

        Node node = root;
        for (int k = 0; k < keys.size() - 1; k++) {
            ColumnView keyColumn = keys.get(k);
            String keyString = keyColumn.getString(rowIndex);
            node = node.getOrInsertChild(nullToEmpty(keyString));
        }

        String leafKeyString = leafKey.getString(rowIndex);
        String id = idColumn.getString(rowIndex);

        node.addKey(leafKeyString, id);
    }

    public String lookup(String... keyStrings) {
        Node node = root;
        int k;
        for (k = 0; k < keys.size() - 1; k++) {
            String keyString = nullToEmpty(keyStrings[k]);
            node = node.findChild(nullToEmpty(keyString));
            if(node == null) {
                return null;
            }
        }
        String leafKey = keyStrings[k];
        return node.findRecord(leafKey);
    }

}
