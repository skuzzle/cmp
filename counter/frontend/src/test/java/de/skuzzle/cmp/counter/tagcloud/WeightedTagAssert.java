package de.skuzzle.cmp.counter.tagcloud;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class WeightedTagAssert extends AbstractAssert<WeightedTagAssert, WeightedTag> {

    private WeightedTagAssert(WeightedTag actual) {
        super(actual, WeightedTagAssert.class);
    }

    public static WeightedTagAssert assertThat(WeightedTag actual) {
        return new WeightedTagAssert(actual);
    }

    public WeightedTagAssert withCount(int count) {
        Assertions.assertThat(actual.getCount()).isEqualTo(count);
        return this;
    }

    public WeightedTagAssert withCssSize(String expected) {
        Assertions.assertThat(actual.getCssSize()).isEqualTo(expected);
        return this;
    }

}
