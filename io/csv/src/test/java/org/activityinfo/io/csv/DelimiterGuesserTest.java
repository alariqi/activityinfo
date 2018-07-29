package org.activityinfo.io.csv;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DelimiterGuesserTest {

    @Test
    public void commaDelimiter() throws IOException {
        String text = Resources.toString(getResource(getClass(), "qis.csv"), Charsets.UTF_8);

        final char delimiter = new DelimiterGuesser(text).guess();

        assertEquals(delimiter, ',');
    }

    @Test
    public void columnsDoesNotMatch() throws IOException    {
        String text = Resources.toString(getResource(getClass(), "qis-invalid.csv"), Charsets.UTF_8);
        DelimiterGuesser guesser = new DelimiterGuesser(text);
        guesser.guess();

        assertTrue(guesser.isDataSetOfOneColumn());
        assertEquals(guesser.getFirstNotMatchedRow(), 2);
    }
}
