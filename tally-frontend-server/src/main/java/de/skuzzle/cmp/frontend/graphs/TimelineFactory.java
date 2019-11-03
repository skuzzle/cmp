package de.skuzzle.cmp.frontend.graphs;

import java.time.LocalDateTime;
import java.util.Collection;

@FunctionalInterface
public interface TimelineFactory {

    Timeline createTimelineFrom(Collection<LocalDateTime> instants);
}
