package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.query.StringArrayColumnView;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SourceColumnTest {

    @Test
    public void dates() {

        SourceColumn column = column("1/1/2015", "foo", "1980-01-04", "25000-03-05");
        assertThat(column.getDateCount(), equalTo(2));
    }

    private SourceColumn column(String... values) {
        return new SourceColumn("A", "B", new StringArrayColumnView(values));
    }

}