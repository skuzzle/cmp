package de.skuzzle.cmp.counter.tagcloud;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import de.skuzzle.cmp.counter.client.RestTallyIncrement;
import de.skuzzle.cmp.counter.client.RestTallyResponse;
import de.skuzzle.cmp.counter.client.Tags;

public class TagCloud {

    private final List<WeightedTag> tags;

    private TagCloud(List<WeightedTag> tags) {
        this.tags = tags;
    }

    public static TagCloud fromBackendResponse(RestTallyResponse response) {
        final List<RestTallyIncrement> increments = response.getIncrements().getEntries();

        final Map<String, Long> tagToCount = increments.stream()
                .map(RestTallyIncrement::getTags)
                .map(Tags::all)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        final int maxCount = Ints.saturatedCast(tagToCount.values().stream().max(Long::compare).orElse(0L));
        final int differentTags = tagToCount.keySet().size();

        final List<WeightedTag> tags = tagToCount.entrySet().stream()
                .map(mapEntry -> toWeightedTag(maxCount, differentTags, mapEntry))
                .collect(Collectors.toList());

        return new TagCloud(tags);
    }

    private static WeightedTag toWeightedTag(int maxCount, int differentTags, Entry<String, Long> mapEntry) {
        Preconditions.checkArgument(maxCount > 0, "maxCount must not be <= 0 but was: %s", maxCount);
        final String tag = mapEntry.getKey();
        final int tagCount = Ints.saturatedCast(mapEntry.getValue());
        return new WeightedTag(tag, tagCount, maxCount, differentTags);
    }

    public List<WeightedTag> getTags() {
        return this.tags;
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }
}
