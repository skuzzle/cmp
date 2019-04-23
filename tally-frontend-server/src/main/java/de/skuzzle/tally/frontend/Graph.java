package de.skuzzle.tally.frontend;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.skuzzle.tally.frontend.client.TallyIncrement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {

    private final List<Dataset> datasets;
    private final List<String> labels;
    private final int pointRadius = 0;

    private Graph(Collection<TallyIncrement> history) {
        final Timeline timeline = new Timeline();
        history.stream()
                .map(TallyIncrement::getIncrementDateUTC)
                .forEach(timeline::extend);

        int[] counter = new int[1];
        final List<String> labels = new ArrayList<>(history.size());
        final List<Point> data = history.stream()
                .sorted(Comparator.comparing(TallyIncrement::getIncrementDateUTC))
                .map(increment -> {
                    final double x = timeline.classify(increment.getIncrementDateUTC());
                    final double y = ++counter[0];
                    labels.add(increment.getDescription());
                    return new Point(x, y);
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

    public int getPointRadius() {
        return pointRadius;
    }

    static class Timeline {
        private LocalDateTime min = LocalDateTime.now();
        private LocalDateTime max = LocalDateTime.now();
        private double length = 0;

        public Timeline extend(LocalDateTime date) {
            if (date.compareTo(min) < 0) {
                min = date;
            } else if (date.compareTo(max) > 0) {
                max = date;
            }

            final Duration diff = Duration.between(min, max);
            this.length = diff.toMillis();
            return this;
        }

        public double classify(LocalDateTime date) {
            Preconditions.checkArgument(date.compareTo(min) >= 0 && date.compareTo(max) <= 0,
                    "Given date %s should be >= %s and <= %s", date, min, max);

            final Duration untilDate = Duration.between(min, date);
            return untilDate.toMillis() / length;
        }

    }

    static class Dataset {
        private final List<Point> data;

        Dataset(List<Point> data) {
            this.data = data;
        }

        public List<Point> getData() {
            return data;
        }
    }

    static class Point {
        private final double x;
        private final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}
