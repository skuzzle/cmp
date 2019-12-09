package de.skuzzle.cmp.frontend.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

public class Tags implements Iterable<String> {

    private final Set<String> tags;

    Tags(Set<String> tags) {
        this.tags = tags;
    }

    public static final Tags fromCollection(Collection<String> tags) {
        Preconditions.checkArgument(tags != null, "tags must not be null");
        return new Tags(Set.copyOf(tags));
    }

    public static final Tags fromString(String tags) {
        Preconditions.checkArgument(tags != null, "tags must not be null");
        final Set<String> tagSet = Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet());
        return new Tags(tagSet);
    }

    @Override
    public Iterator<String> iterator() {
        return tags.iterator();
    }

    public Set<String> all() {
        return tags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Tags
                && Objects.equals(tags, ((Tags) obj).tags);
    }

    @Override
    public String toString() {
        return tags.stream().collect(Collectors.joining(","));
    }
}
