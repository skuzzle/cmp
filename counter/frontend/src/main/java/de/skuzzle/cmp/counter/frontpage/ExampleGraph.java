package de.skuzzle.cmp.counter.frontpage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.skuzzle.cmp.counter.client.RestTallyIncrement;
import de.skuzzle.cmp.counter.graphs.Graph;

class ExampleGraph {
    
    static Graph randomGraph() {
        final List<RestTallyIncrement> increments = IntStream.range(1, 12)
                .mapToObj(month -> forMonth(month, (int) (Math.random() * 10 + 5)))
                .flatMap(Function.identity())
                .collect(Collectors.toList());
        return Graph.fromHistory(increments);
    }
    
    static Stream<RestTallyIncrement> forMonth(int month, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> RestTallyIncrement.createNew("", LocalDateTime.of(2019, month, 1,0,0), Set.of()));
    }
}
