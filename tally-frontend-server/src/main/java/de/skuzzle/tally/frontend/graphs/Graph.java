package de.skuzzle.tally.frontend.graphs;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.skuzzle.tally.frontend.client.TallyIncrement;

public class Graph {

    private final List<Dataset> datasets;
    private final List<String> labels;

    private Graph(Collection<TallyIncrement> history) {
        this.labels = history.stream()
                .sorted(Comparator.comparing(TallyIncrement::getIncrementDateUTC))
                .map(TallyIncrement::getDescription)
                .collect(Collectors.toList());

        final Dataset monthly = Dataset.create(history, MonthBucketTimeline::new);
        datasets = List.of(monthly);
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
