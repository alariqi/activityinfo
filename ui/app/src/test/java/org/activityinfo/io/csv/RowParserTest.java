package org.activityinfo.io.csv;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RowParserTest {



    @Test
    public void simpleCsv() {

        List<ParsedRow> rows = new RowParser("hello,brave,new,world\n1,2,3,4\n5,6,7,8", ',').parseRows();

        assertThat(rows.size(), equalTo(3));
        assertThat(rows.get(0).getColumnValue(0), equalTo("hello"));
        assertThat(rows.get(0).getColumnValue(1), equalTo("brave"));
        assertThat(rows.get(0).getColumnValue(3), equalTo("world"));
        assertThat(rows.get(1).getColumnValue(0), equalTo("1"));
        assertThat(rows.get(1).getColumnValue(1), equalTo("2"));
        assertThat(rows.get(1).getColumnValue(3), equalTo("4"));
        assertThat(rows.get(2).getColumnValue(3), equalTo("8"));
    }

    @Test
    public void quotedFields() {
        List<ParsedRow> rows = new RowParser("\"hello, fred\",bob,\"hello, there\"\na,b,c", ',').parseRows();

        assertThat(rows.size(), equalTo(2));
        assertThat(rows.get(0).getColumnValue(0), equalTo("hello, fred"));
        assertThat(rows.get(0).getColumnValue(1), equalTo("bob"));
        assertThat(rows.get(0).getColumnValue(2), equalTo("hello, there"));
        assertThat(rows.get(1).getColumnValue(0), equalTo("a"));
    }

    @Test
    public void quotedFieldsWithNewlines() {
        List<ParsedRow> rows = new RowParser(
                "1,Jane Goodall,\"304 E42nd street\nNew York, NY\nUSA\"\n" +
                        "2,Richard Feynman,\"401 1st Street\nCaltech\nUSA\"", ',').parseRows();

        System.out.println(Joiner.on('\n').join(rows));

        assertThat(rows.size(), equalTo(2));
        assertThat(rows.get(0).getColumnValue(2), equalTo("304 E42nd street\n" +
                "New York, NY\n" +
                "USA"));

        assertThat(rows.get(1).getColumnValue(0), equalTo("2"));
    }

    @Test
    public void quotedFieldsWithCrNewlines() {
        List<ParsedRow> rows = new RowParser(
                "1,Jane Goodall,\"304 E42nd street\r\nNew York, NY\r\nUSA\"\r\n" +
                        "2,Richard Feynman,\"401 1st Street\r\nCaltech\r\nUSA\"", ',').parseRows();

        assertThat(rows.size(), equalTo(2));
        assertThat(rows.get(0).getColumnValue(2), equalTo("304 E42nd street\r\n" +
                "New York, NY\r\n" +
                "USA"));

        assertThat(rows.get(1).getColumnValue(0), equalTo("2"));
    }

    @Test
    public void quotesInTheMiddle() {
        String text = "1,2,\"Il s'agit d'une intervention humanitaire en faveurs des Congolais refoules de l\"\"angola\"";
        List<ParsedRow> rows = new RowParser(text, ',').parseRows();

        assertThat(rows, hasSize(1));

        ParsedRow firstRow = rows.get(0);
        assertThat(firstRow.getColumnValue(0), equalTo("1"));
        assertThat(firstRow.getColumnValue(1), equalTo("2"));
        assertThat(firstRow.getColumnValue(2), equalTo(
                "Il s'agit d'une intervention humanitaire en faveurs des Congolais refoules de l\"angola"));
    }


    @Test
    public void trailingNewLine() throws IOException {
        URL resource = Resources.getResource(ImportViewModel.class, "qis-villages.csv");
        String text = Resources.toString(resource, Charsets.UTF_8);

        RowParser parser = new RowParser(text, ',');
        List<ParsedRow> rows = parser.parseRows();

        assertThat(rows.get(0).getColumnValue(0), equalTo("Name"));
        assertThat(rows.get(0).getColumnValue(1), equalTo("District"));
        assertThat(rows.get(1).getColumnValue(0), equalTo("Village 1"));
        assertThat(rows.get(1).getColumnValue(1), equalTo("Chittagong"));
    }
}