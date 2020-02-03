package de.skuzzle.cmp.counter.tagcloud;

import de.skuzzle.cmp.counter.client.Filter;

public class WeightedTag {

    private final String name;
    private final int count;
    private final int differentCounts;
    private final double weight;

    WeightedTag(String name, int count, int maxCount, int differentCounts) {
        this.name = name;
        this.count = count;
        this.differentCounts = differentCounts;
        this.weight = (double) count / maxCount;
    }

    public String getName() {
        return this.name;
    }

    public int getCount() {
        return this.count;
    }

    public boolean isSelected(TagCloud cloud) {
        return cloud.filter().containsTag(name);
    }

    public String getSelectLink(TagCloud cloud) {
        final Filter updatedFilter = cloud.filter().addTag(name);
        return updatedFilter.getCounterLink(cloud.getCounterKey());
    }

    public String getDeleteLink(TagCloud cloud) {
        final Filter updatedFilter = cloud.filter().removeTag(name);
        return updatedFilter.getCounterLink(cloud.getCounterKey());
    }

    public String getCssSize() {
        // smallest
        final int maxBulmaSize = 7;
        final int buckets = Math.min(differentCounts, 5);
        // [0, buckets - 1]
        final int relativeSize = relativeSize(buckets);

        final int bulmaSize = maxBulmaSize - relativeSize;
        return "is-size-" + bulmaSize;
    }

    private int relativeSize(int buckets) {
        for (int i = 1; i <= buckets; ++i) {
            final double part = i / (double) buckets;
            if (weight <= part) {
                return i - 1;
            }
        }
        throw new IllegalStateException();
    }
}
