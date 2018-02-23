package chdc.frontend.client.cheatsheet;

import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrixColumnSet;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches only on the leaf key for matching keys
 */
public class SearchContains implements SearchFunction {


    @Override
    public List<LabeledRecordRef> search(LookupKeySet keySet, KeyMatrixColumnSet keyMatrix, String query) {

        String loweredQuery = query.toLowerCase();

        ResourceId formId = keyMatrix.getFormId();

        ColumnView id = keyMatrix.getId();
        ColumnView keyValues = keyMatrix.getKey(keySet.getLeafKey(formId));

        List<LabeledRecordRef> results = new ArrayList<>();

        int numRows = keyValues.numRows();
        for (int i = 0; i < numRows; i++) {
            String key = keyValues.getString(i);
            if(key != null && key.toLowerCase().contains(loweredQuery)) {
                results.add(new LabeledRecordRef(formId, id.getString(i), key));
            }
        }

        return results;
    }
}
