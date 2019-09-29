package de.skuzzle.tally.frontend.graphs;

import java.util.List;

public class Dataset {
    private final List<Point> data;
    private final int pointRadius = 0;

    Dataset(List<Point> data) {
        this.data = data;
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public List<Point> getData() {
        return data;
    }
}
