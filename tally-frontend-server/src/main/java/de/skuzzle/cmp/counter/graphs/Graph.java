package de.skuzzle.cmp.counter.graphs;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.skuzzle.cmp.counter.client.RestTallyIncrement;

public class Graph {

    private final List<Dataset> datasets;
    private final List<String> labels;

    private Graph(Collection<RestTallyIncrement> history) {
        this.labels = history.stream()
                .sorted(Comparator.comparing(RestTallyIncrement::getIncrementDateUTC))
                .map(RestTallyIncrement::getDescription)
                .collect(Collectors.toList());

        final Dataset monthly = Dataset.create(history, MonthInYearBucketTimeline::new);
        datasets = List.of(monthly);
    }

    public static Graph fromHistory(Collection<RestTallyIncrement> history) {
        return new Graph(history);
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public List<String> getLabels() {
        return labels;
    }
}
