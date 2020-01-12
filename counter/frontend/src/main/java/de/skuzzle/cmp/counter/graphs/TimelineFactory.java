package de.skuzzle.cmp.counter.graphs;

import java.time.LocalDateTime;
import java.util.Collection;

@FunctionalInterface
public interface TimelineFactory {

    Timeline createTimelineFrom(Collection<LocalDateTime> instants);
}
