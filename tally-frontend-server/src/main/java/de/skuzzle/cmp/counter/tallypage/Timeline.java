package de.skuzzle.cmp.counter.tallypage;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import de.skuzzle.cmp.counter.client.RestTallyIncrement;
import de.skuzzle.cmp.counter.client.RestTallyResponse;

public class Timeline {

    private final List<TimelineYear> years;

    private Timeline(List<TimelineYear> years) {
        this.years = years;
    }

    public static Timeline fromBackendResponse(RestTallyResponse response) {
        final List<RestTallyIncrement> increments = response.getIncrements().getEntries();

        final Map<YearMonth, List<TimelineIncrement>> byMonth = increments.stream()
                .map(TimelineIncrement::fromBackendResponse)
                .sorted(comparing(TimelineIncrement::getIncrementDateUTC).reversed())
                .collect(groupingBy(TimelineIncrement::getYearMonth));

        final List<TimelineMonth> months = byMonth.entrySet().stream()
                .map(Timeline::toMonth)
                .sorted(comparing(TimelineMonth::getYearMonth).reversed())
                .collect(toList());

        final Map<Year, List<TimelineMonth>> byYear = months.stream()
                .sorted(comparing(TimelineMonth::getYearMonth).reversed())
                .collect(groupingBy(TimelineMonth::getYear));

        final List<TimelineYear> years = byYear.entrySet().stream()
                .map(Timeline::toYear)
                .sorted(comparing(TimelineYear::getYear).reversed())
                .collect(toList());
        return new Timeline(years);
    }

    private static TimelineYear toYear(Entry<Year, List<TimelineMonth>> entry) {
        final Year year = entry.getKey();
        final List<TimelineMonth> months = entry.getValue();
        return new TimelineYear(year.toString(), year, months);
    }

    private static TimelineMonth toMonth(Entry<YearMonth, List<TimelineIncrement>> entry) {
        final YearMonth yearMonth = entry.getKey();
        final Month month = yearMonth.getMonth();
        final List<TimelineIncrement> increments = entry.getValue();
        return new TimelineMonth(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH), yearMonth, increments);
    }

    public List<TimelineYear> getYears() {
        return this.years;
    }
}
