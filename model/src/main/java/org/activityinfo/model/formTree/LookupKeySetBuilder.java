package org.activityinfo.model.formTree;

import org.activityinfo.model.expr.SymbolExpr;
import org.activityinfo.model.resource.ResourceId;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs a set of lookup keys that can be used to locate a specific
 * record from among one or more forms.
 */
public class LookupKeySetBuilder {

    private int nextKeyIndex = 0;
    private LookupKey parentLevel = null;

    private List<LookupKey> keys = new ArrayList<>();

    public LookupKeySetBuilder() {
    }

    public LookupKeySetBuilder add(String formId, String key) {
        LookupKey lookupKey = new LookupKey(nextKeyIndex++, null, parentLevel,
                ResourceId.valueOf(formId), formId, key, new SymbolExpr(key));

        parentLevel = lookupKey;
        keys.add(parentLevel);
        return this;
    }

    public LookupKeySetBuilder add(String formId, String parentField, String key) {
        LookupKey lookupKey = new LookupKey(nextKeyIndex++, parentField, parentLevel,
                ResourceId.valueOf(formId), formId, key, new SymbolExpr(key));

        parentLevel = lookupKey;
        keys.add(parentLevel);
        return this;
    }

    public LookupKeySet build() {
        return new LookupKeySet(keys);
    }

}
