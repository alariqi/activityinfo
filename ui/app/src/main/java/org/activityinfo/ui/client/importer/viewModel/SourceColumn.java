package org.activityinfo.ui.client.importer.viewModel;

import com.google.gwt.regexp.shared.RegExp;
import org.activityinfo.io.match.date.LatinDateParser;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.type.time.LocalDate;

public class SourceColumn {

    private static final RegExp NUMBER_REGEX = RegExp.compile("^[0-9.,]+$");
    private static final RegExp ANY_DIGITS = RegExp.compile("\\d");
    private static final LatinDateParser DATE_PARSER = new LatinDateParser();

    private String id;
    private String label;
    private ColumnView columnView;

    private int missingCount;
    private int numberCount;
    private int dateCount;

    /**
     * Number of rows that are not missing
     */
    private double validRows;

    public SourceColumn(String id, String label, ColumnView columnView) {
        this.id = id;
        this.label = label;
        this.columnView = columnView;

        // Compile statistics on this column that can be used to match
        // column types efficiently
        for (int i = 0; i < columnView.numRows(); i++) {
            String value = columnView.getString(i);
            if(value == null) {
                missingCount++;
            } else {
                if(ANY_DIGITS.test(value)) {
                    if(NUMBER_REGEX.test(value)) {
                        numberCount++;
                    }
                    // Shortest date is "1/1/80"
                    if(value.length() >= 6 && isDate(value)) {
                        dateCount++;
                    }
                }
            }
        }

        validRows = columnView.numRows() - missingCount;
    }

    private boolean isDate(String value) {
        try {
            LocalDate date = DATE_PARSER.parse(value);
            return date.getYear() > 1000 && date.getYear() <= 9999;
        } catch (IllegalArgumentException e) {
            return false;
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
        if(validRows == 0) {
            return 0;
        } else {
            return numberCount / validRows;
        }
    }

    public int getMissingCount() {
        return missingCount;
    }

    public int getNumberCount() {
        return numberCount;
    }

    public int getDateCount() {
        return dateCount;
    }

    public double getDateFraction() {
        if(validRows == 0) {
            return 0;
        } else {
            return dateCount / validRows;
        }
    }

    public boolean hasNumbers() {
        return numberCount > 0;
    }
}
