package de.skuzzle.tally.frontend.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import de.skuzzle.tally.frontend.client.TallyIncrement;

public class Graph {

    private final List<Dataset> datasets;
    private final List<String> labels;

    private Graph(Collection<TallyIncrement> history) {
        final Timeline timeline = MonthBucketTimeline.initializeFrom(history.stream()
                .map(TallyIncrement::getIncrementDateUTC));

        final List<String> labels = new ArrayList<>(history.size());
        final List<Point> data = history.stream()
                .sorted(Comparator.comparing(TallyIncrement::getIncrementDateUTC))
                .map(increment -> {
                    final Point point = timeline.classify(increment.getIncrementDateUTC());
                    labels.add(increment.getDescription());
                    return point;
                })
                .collect(Collectors.toList());
        this.datasets = ImmutableList.of(new Dataset(data));
        this.labels = labels;
    }

    public static Graph fromHistory(Collection<TallyIncrement> history) {
        return new Graph(history);
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public List<String> getLabels() {
        return labels;
    }
}
