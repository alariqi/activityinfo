package org.activityinfo.ui.client.importer.viewModel;

import com.google.gwt.regexp.shared.RegExp;
import org.activityinfo.model.query.ColumnView;

public class SourceColumn {

    private static final RegExp NUMBER_REGEX = RegExp.compile("[0-9.,]+");

    private String id;
    private String label;
    private ColumnView columnView;

    private int missingCount;
    private int numberCount;

    public SourceColumn(String id, String label, ColumnView columnView) {
        this.id = id;
        this.label = label;
        this.columnView = columnView;

        for (int i = 0; i < columnView.numRows(); i++) {
            String value = columnView.getString(i);
            if(value == null) {
                missingCount++;
            } else {
                if(NUMBER_REGEX.test(value)) {
                    numberCount++;
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public ColumnView getColumnView() {
        return columnView;
    }

    /**
     * @return the fraction of non-empty rows that could be numbers
     */
    public double getNumberFraction() {
        double validRows = columnView.numRows() - missingCount;
        if(validRows == 0) {
            return 0;
        } else {
            return numberCount / validRows;
        }
    }

    public boolean hasNumbers() {
        return numberCount > 0;
    }
}
