package de.skuzzle.cmp.common.collections;

import java.util.List;
import java.util.function.Predicate;

public final class Lists {

    public static <T> int firstIndexOf(List<T> list, Predicate<? super T> p) {
        for (int i = 0; i < list.size(); i++) {
            final T element = list.get(i);
            if (p.test(element)) {
                return i;
            }
        }
        return -1;
    }

    private Lists() {
        // hidden
    }
}
