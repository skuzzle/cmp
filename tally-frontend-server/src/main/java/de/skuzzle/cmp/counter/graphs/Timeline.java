package de.skuzzle.cmp.counter.graphs;

import java.time.LocalDateTime;

public interface Timeline {

    Point classify(LocalDateTime instant);
}
