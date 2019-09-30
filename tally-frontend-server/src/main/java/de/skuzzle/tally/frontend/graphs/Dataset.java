package de.skuzzle.tally.frontend.graphs;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.skuzzle.tally.frontend.client.TallyIncrement;

public class Dataset {
    private final List<Point> data;
    private final int pointRadius = 0;

    Dataset(List<Point> data) {
        this.data = data;
    }

    public static Dataset create(Collection<TallyIncrement> history, TimelineFactory tlFactory) {
        final List<LocalDateTime> instants = history.stream()
                .map(TallyIncrement::getIncrementDateUTC)
                .collect(Collectors.toList());
        final Timeline timeline = tlFactory.createTimelineFrom(instants);
        final List<Point> data = history.stream()
                .map(TallyIncrement::getIncrementDateUTC)
                .sorted()
                .map(timeline::classify)
                .collect(Collectors.toList());
        return new Dataset(data);
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public List<Point> getData() {
        return data;
    }
}
