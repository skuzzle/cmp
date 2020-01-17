package de.skuzzle.cmp.counter.timeline;

import static de.skuzzle.cmp.counter.timeline.TimelineAssert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Test;

import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.TestResponses;

public class TimelineBuilderTest {

    @Test
    void testTimelineReadOnly() throws Exception {
        final RestTallyResponse restTallyResponse = TestResponses.tallySheet().withAdminKey(null).toResponse();
        final Timeline timeline = TimelineBuilder.fromBackendResponse(restTallyResponse);

        assertThat(timeline).isReadOnly();
    }

    @Test
    void testTimelineAdmin() throws Exception {
        final RestTallyResponse restTallyResponse = TestResponses.tallySheet().withAdminKey("adminKey").toResponse();
        final Timeline timeline = TimelineBuilder.fromBackendResponse(restTallyResponse);

        assertThat(timeline).isAdmin("adminKey");
    }

    @Test
    void testIncrementsOnDifferentYears() throws Exception {
        final LocalDateTime date19 = LocalDate.of(2019, 02, 03).atStartOfDay();
        final LocalDateTime date20 = LocalDate.of(2020, 02, 03).atStartOfDay();
        final RestTallyResponse restTallyResponse = TestResponses.tallySheet()
                .addIncrement("1", "1", date19)
                .addIncrement("2", "2", date20)
                .toResponse();

        final Timeline timeline = TimelineBuilder.fromBackendResponse(restTallyResponse);
        assertThat(timeline).year(2019).month(Month.FEBRUARY).hasTotalCount(1);
        assertThat(timeline).year(2020).month(Month.FEBRUARY).hasTotalCount(1);
    }

    @Test
    void testGroupIncrementsOnSameDay() throws Exception {
        final LocalDateTime someDay = LocalDate.of(1987, 12, 9).atStartOfDay();
        final RestTallyResponse restTallyResponse = TestResponses.tallySheet()
                .addIncrement("1", "1", someDay)
                .addIncrement("2", "2", someDay)
                .toResponse();
        final Timeline timeline = TimelineBuilder.fromBackendResponse(restTallyResponse);
        assertThat(timeline).onlyYear().onlyMonth().onlyDay()
                .hasTotalCount(2)
                .hasDateUTC(someDay);
    }
}
