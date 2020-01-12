package de.skuzzle.cmp.counter.collections;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Predicate;

public final class Lists {

    public static <T> OptionalInt firstIndexOf(List<T> list, Predicate<? super T> p) {
        for (int i = 0; i < list.size(); i++) {
            final T element = list.get(i);
            if (p.test(element)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private Lists() {
        // hidden
    }
}
