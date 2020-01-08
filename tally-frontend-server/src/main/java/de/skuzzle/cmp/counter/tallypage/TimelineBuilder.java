package de.skuzzle.cmp.counter.tallypage;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.skuzzle.cmp.counter.client.RestTallyIncrement;
import de.skuzzle.cmp.counter.client.RestTallyResponse;

class TimelineBuilder {

    public static Timeline fromBackendResponse(RestTallyResponse response) {
        final TimelineBuilder builder = new TimelineBuilder();
        response.getIncrements().getEntries().forEach(builder::addIncrement);
        return builder.toTimeline();
    }

    private final Map<Year, TimelineYearBuilder> years = new HashMap<>();

    private TimelineBuilder addIncrement(RestTallyIncrement increment) {
        final Year year = Year.from(increment.getIncrementDateUTC());
        final TimelineYearBuilder yearBuilder = years.computeIfAbsent(year, TimelineYearBuilder::new);
        yearBuilder.addIncrement(increment);
        return this;
    }

    private Timeline toTimeline() {
        final List<TimelineYear> timelineYears = years.values().stream()
                .sorted(comparing(TimelineYearBuilder::getYear).reversed())
                .map(TimelineYearBuilder::toTimelineYear)
                .collect(toList());
        return new Timeline(timelineYears);

    }

    private static class TimelineYearBuilder {
        private final Year year;
        private final Map<YearMonth, TimelineMonthBuilder> months = new HashMap<>();

        TimelineYearBuilder(Year year) {
            this.year = year;
        }

        private Year getYear() {
            return this.year;
        }

        private void addIncrement(RestTallyIncrement increment) {
            final YearMonth yearMonth = YearMonth.from(increment.getIncrementDateUTC());
            final TimelineMonthBuilder monthBuilder = months.computeIfAbsent(yearMonth, TimelineMonthBuilder::new);
            monthBuilder.addIncrement(increment);
        }

        private TimelineYear toTimelineYear() {
            final List<TimelineMonth> timelineMonths = months.values().stream()
                    .sorted(comparing(TimelineMonthBuilder::getMonth).reversed())
                    .map(TimelineMonthBuilder::toTimelineMonth)
                    .collect(toList());
            return new TimelineYear(year.toString(), year, timelineMonths);
        }
    }

    private static class TimelineMonthBuilder {
        private final YearMonth yearMonth;
        private final Map<MonthDay, TimlineDayBuilder> days = new HashMap<>();

        TimelineMonthBuilder(YearMonth month) {
            this.yearMonth = month;
        }

        private YearMonth getMonth() {
            return this.yearMonth;
        }

        private void addIncrement(RestTallyIncrement increment) {
            final MonthDay monthDay = MonthDay.from(increment.getIncrementDateUTC());
            final TimlineDayBuilder dayBuilder = days.computeIfAbsent(monthDay, TimlineDayBuilder::new);
            dayBuilder.addIncrement(increment);
        }

        private TimelineMonth toTimelineMonth() {
            final List<TimelineDay> timelineDays = days.values().stream()
                    .sorted(comparing(TimlineDayBuilder::getDay).reversed())
                    .map(TimlineDayBuilder::toTimelineDay)
                    .collect(toList());
            return new TimelineMonth(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), yearMonth,
                    timelineDays);

        }
    }

    private static class TimlineDayBuilder {
        private final MonthDay monthDay;
        private final List<RestTallyIncrement> increments = new ArrayList<>();

        TimlineDayBuilder(MonthDay day) {
            this.monthDay = day;
        }

        private MonthDay getDay() {
            return this.monthDay;
        }

        private void addIncrement(RestTallyIncrement increment) {
            increments.add(increment);
        }

        private TimelineDay toTimelineDay() {
            final LocalDateTime dateUTC = increments.iterator().next().getIncrementDateUTC();
            final List<TimelineIncrement> timelineIncrements = increments.stream()
                    .sorted(comparing(RestTallyIncrement::getIncrementDateUTC).reversed())
                    .map(TimelineIncrement::fromBackendResponse)
                    .collect(toList());
            return new TimelineDay(
                    DayOfWeek.from(dateUTC).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                    dateUTC,
                    timelineIncrements);
        }

    }
}
