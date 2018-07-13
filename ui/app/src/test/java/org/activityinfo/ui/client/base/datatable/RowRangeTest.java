package org.activityinfo.ui.client.base.datatable;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RowRangeTest {

    @Test
    public void contains() {

        RowRange a = new RowRange(0, 50);
        RowRange b = new RowRange(10, 5);

        assertTrue(a.contains(b));
        assertFalse(b.contains(a));
    }

}