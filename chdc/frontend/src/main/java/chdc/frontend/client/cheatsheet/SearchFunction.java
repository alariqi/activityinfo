package chdc.frontend.client.cheatsheet;

import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrixColumnSet;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;

import java.util.List;

/**
 * Searches a key matrix for a text value
 */
public interface SearchFunction {

    List<LabeledRecordRef> search(LookupKeySet keySet, KeyMatrixColumnSet keyMatrix, String query);

}
