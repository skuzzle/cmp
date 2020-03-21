package de.skuzzle.cmp.counter.domain;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ShareInformation {

    public static final ShareInformation INCREMENTS_WITHOUT_DETAILS = new ShareInformation(true, false, false);

    private final boolean showIncrements;
    private final boolean showIncrementTags;
    private final boolean showIncrementTitle;

    private ShareInformation(boolean showIncrements, boolean showIncrementTags, boolean showIncrementTitle) {
        this.showIncrements = showIncrements;
        this.showIncrementTags = showIncrementTags;
        this.showIncrementTitle = showIncrementTitle;
    }

    public static ShareInformationBuilder builder() {
        return new ShareInformationBuilder();
    }

    public List<TallyIncrement> getIncrements(List<TallyIncrement> increments) {
        return showIncrements
                ? increments.stream().map(increment -> increment.wipeFor(this))
                        .collect(Collectors.toList())
                : Collections.emptyList();
    }

    public String getIncrementTitle(String incrementTitle) {
        return showIncrements && showIncrementTitle
                ? incrementTitle
                : "<hidden>";
    }

    public Set<String> getIncrementTags(Set<String> tags) {
        return showIncrements && showIncrementTags
                ? tags
                : Collections.emptySet();
    }

    public static class ShareInformationBuilder {
        private boolean showIncrements;
        private boolean showIncrementTags;
        private boolean showIncrementTitle;

        private ShareInformationBuilder() {
            // hidden
        }

        public ShareInformationBuilder showIncrements(boolean showIncrements) {
            this.showIncrements = showIncrements;
            return this;
        }

        public ShareInformationBuilder showIncrementTags(boolean showIncrementTags) {
            this.showIncrementTags = showIncrementTags;
            return this;
        }

        public ShareInformationBuilder showIncrementDescription(boolean showIncrementTitle) {
            this.showIncrementTitle = showIncrementTitle;
            return this;
        }

        public ShareInformation build() {
            return new ShareInformation(showIncrements, showIncrementTags, showIncrementTitle);
        }
    }
}
