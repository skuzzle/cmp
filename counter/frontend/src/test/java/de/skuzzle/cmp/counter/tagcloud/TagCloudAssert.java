package de.skuzzle.cmp.counter.tagcloud;

import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import de.skuzzle.cmp.counter.tagcloud.TagCloud;
import de.skuzzle.cmp.counter.tagcloud.WeightedTag;

public class TagCloudAssert extends AbstractAssert<TagCloudAssert, TagCloud> {

    private TagCloudAssert(TagCloud actual) {
        super(actual, TagCloudAssert.class);
    }

    public static TagCloudAssert assertThat(TagCloud actual) {
        return new TagCloudAssert(actual);
    }

    public TagCloudAssert hasTags(String... expectedTags) {
        Assertions.assertThat(actual.getNames()).containsExactlyInAnyOrder(expectedTags);
        final Set<String> weightedTagNames = actual.getTags().stream().map(WeightedTag::getName)
                .collect(Collectors.toSet());
        Assertions.assertThat(weightedTagNames).containsExactlyInAnyOrder(expectedTags);
        return this;
    }

    public WeightedTagAssert hasTagWithName(String name) {
        Assertions.assertThat(actual.getNames()).contains(name);
        return WeightedTagAssert.assertThat(getTag(name));
    }

    private WeightedTag getTag(String name) throws AssertionError {
        final WeightedTag weightedTag = actual.getTags().stream()
                .filter(wt -> wt.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No tag with name " + name));
        return weightedTag;
    }

}
