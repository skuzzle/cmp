package de.skuzzle.cmp.frontend.graphs;

import java.time.LocalDateTime;

public interface Timeline {

    Point classify(LocalDateTime instant);
}
