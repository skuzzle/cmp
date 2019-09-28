package de.skuzzle.tally;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import org.junit.Test;

public class LearningTest {

    @Test
    public void testDeserializeDate() {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        final TemporalAccessor accessor = dateTimeFormatter.parse("2019-04-12T11:21:32.123");
        System.out.println(accessor);

        LocalDateTime.from(accessor);
    }
}
