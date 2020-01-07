package de.skuzzle.cmp.counter.tallypage;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.skuzzle.cmp.counter.client.RestTallyResponse;

public class Timeline {

    private final List<TimelineMonth> months;

    private Timeline(List<TimelineMonth> months) {
        this.months = months;
    }

    public static Timeline fromBackendResponse(RestTallyResponse response) {
        final Map<YearMonth, List<TimelineIncrement>> byMonth = response.getIncrements().getEntries().stream()
                .map(TimelineIncrement::fromBackendResponse)
                .sorted(Comparator.comparing(TimelineIncrement::getIncrementDateUTC).reversed())
                .collect(Collectors.groupingBy(TimelineIncrement::getYearMonth));

        final List<TimelineMonth> months = byMonth.entrySet().stream()
                .map(Timeline::toMonth)
                .sorted(Comparator.comparing(TimelineMonth::getYearMonth).reversed())
                .collect(Collectors.toList());
        return new Timeline(months);
    }

    private static TimelineMonth toMonth(Entry<YearMonth, List<TimelineIncrement>> entry) {
        final YearMonth yearMonth = entry.getKey();
        final Month month = yearMonth.getMonth();
        final List<TimelineIncrement> increments = entry.getValue();
        return new TimelineMonth(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH), yearMonth, increments);
    }

    public List<TimelineMonth> getMonths() {
        return this.months;
    }
}
