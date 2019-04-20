package de.skuzzle.tally;

import de.skuzzle.tally.service.TallySheet;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;

public class LearningTest {

    @Test
    public void testDeserializeDate() {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        final TemporalAccessor accessor = dateTimeFormatter.parse("2019-04-12T11:21:32.123");
        System.out.println(accessor);

        LocalDateTime.from(accessor);
    }
}
