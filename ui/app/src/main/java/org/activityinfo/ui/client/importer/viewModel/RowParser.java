/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.importer.viewModel;

import com.google.gwt.core.client.GWT;
import org.activityinfo.model.query.ColumnView;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses delimited text files into rows and columns
 */
public class RowParser {

    public static final char QUOTE_CHAR = '"';


    public interface EventListener {
        void field(String string);
        boolean endOfRecord();
        void endOfFile();
    }

    private String text;
    private int length;
    private int currentPos = 0;
    private char delimiter;
    private String delimiterString;
    private int rowIndex;
    private int maxRowCount = Integer.MAX_VALUE;

    public RowParser(String text, char delimiter) {
        this.text = text;
        this.length = text.length();
        this.delimiter = delimiter;
        this.delimiterString = "" + delimiter;
    }

    public List<ParsedRow> parseRows() {
        return parseRows(Integer.MAX_VALUE);
    }

    public List<ParsedRow> parseRows(int maxRowCount) {


        List<ParsedRow> rows = new ArrayList<>();
        parse(new EventListener() {

            List<String> cells = new ArrayList<>();

            @Override
            public void field(String string) {
                cells.add(string);
            }

            @Override
            public boolean endOfRecord() {
                rows.add(new ParsedRow(cells));
                cells = new ArrayList<>();
                return rows.size() < maxRowCount;
            }

            @Override
            public void endOfFile() {
            }
        });

        return rows;
    }

    public void parse(EventListener eventListener) {

        int textLength = text.length();
        int currentPos = this.currentPos;

        while(currentPos < textLength) {

            // Start of column
            char c = text.charAt(currentPos);
            boolean lastColumn = false;

            if(c == QUOTE_CHAR) {

                currentPos++;
                int columnStart = currentPos;
                int columnEnd;

                while(true) {
                    int nextQuote = text.indexOf(QUOTE_CHAR, currentPos);
                    if(nextQuote == -1) {
                        // unterminated quoted column
                        columnEnd = textLength;
                        break;
                    }
                    String followingQuote = text.substring(nextQuote + 1, Math.min(textLength, nextQuote + 3));
                    if(followingQuote.startsWith(delimiterString)) {
                        columnEnd = nextQuote;
                        currentPos = nextQuote + 2;
                        break;
                    }
                    if(followingQuote.isEmpty()) {
                        columnEnd = nextQuote;
                        currentPos = textLength;
                        lastColumn = true;
                        break;
                    }
                    if(followingQuote.startsWith("\n")) {
                        columnEnd = nextQuote;
                        currentPos = columnEnd + 2;
                        lastColumn = true;
                        break;
                    }
                    if(followingQuote.startsWith("\r\n")) {
                        columnEnd = nextQuote;
                        lastColumn = true;
                        currentPos = columnEnd + 3;
                        break;
                    }
                }

                eventListener.field(text.substring(columnStart, columnEnd));


            } else {

                // Find end of unquoted field
                int columnStart = currentPos;
                int columnEnd = text.indexOf(delimiter, columnStart);
                int lineEnd = text.indexOf("\n", columnStart);

                if(columnEnd < 0 || (lineEnd > 0 && lineEnd < columnEnd)) {
                    columnEnd = lineEnd;
                    lastColumn = true;
                }
                if(columnEnd == -1) {
                    columnEnd = textLength;
                    lastColumn = true;
                }

                // Advance the cursor to the start of the next field
                currentPos = columnEnd + 1;

                // Backup if there is an \r at the end of this column
                if(lastColumn) {
                    if(text.charAt(columnEnd - 1) == '\r') {
                        columnEnd --;
                    }
                }

                eventListener.field(text.substring(columnStart, columnEnd));
            }

            if(lastColumn) {
                boolean continueParsing = eventListener.endOfRecord();
                if(!continueParsing) {
                    this.currentPos = currentPos;
                    return;
                }
            }
        }

        this.currentPos = textLength;
        eventListener.endOfFile();
    }

    public ColumnView[] parseColumns(int columnCount) {
        ColumnBuilder[] columns = new ColumnBuilder[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columns[i] = createBuilder();
        }

        parse(new EventListener() {

            int columnIndex = 0;

            @Override
            public void field(String string) {
                columns[columnIndex++].add(string);
            }

            @Override
            public boolean endOfRecord() {
                while(columnIndex < columnCount) {
                    columns[columnIndex++].add(null);
                }
                columnIndex = 0;
                return true;
            }

            @Override
            public void endOfFile() {
            }
        });

        ColumnView[] views = new ColumnView[columnCount];
        for (int i = 0; i < columnCount; i++) {
            views[i] = columns[i].build();
        }
        return views;
    }

    private ColumnBuilder createBuilder() {
        if(GWT.isScript()) {
            return JsColumnBuilder.create();
        } else {
            return new JvmColumnBuilder();
        }
    }
}
