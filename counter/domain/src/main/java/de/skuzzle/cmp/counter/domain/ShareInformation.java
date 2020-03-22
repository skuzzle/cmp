package de.skuzzle.cmp.counter.domain;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;

public final class ShareInformation {

    public static final ShareInformation INCREMENTS_WITHOUT_DETAILS = new ShareInformation(true, false, false);
    public static final ShareInformation ALL_DETAILS = new ShareInformation(true, true, true);

    private final boolean showIncrements;
    private final boolean showIncrementTags;
    private final boolean showIncrementDescription;

    private ShareInformation(boolean showIncrements, boolean showIncrementTags, boolean showIncrementDescription) {
        this.showIncrements = showIncrements;
        this.showIncrementTags = showIncrementTags;
        this.showIncrementDescription = showIncrementDescription;
    }

    boolean isShowIncrements() {
        return this.showIncrements;
    }

    boolean isShowIncrementTags() {
        return this.showIncrementTags;
    }

    boolean isShowIncrementDescription() {
        return this.showIncrementDescription;
    }

    public static ShareInformationBuilder builder() {
        return new ShareInformationBuilder();
    }

    public List<TallyIncrement> getIncrements(List<TallyIncrement> increments) {
        return showIncrements
                ? increments.stream().map(increment -> increment.wipedCopyFor(this))
                        .collect(Collectors.toList())
                : Collections.emptyList();
    }

    public String getIncrementTitle(String incrementTitle) {
        return showIncrements && showIncrementDescription
                ? incrementTitle
                : "";
    }

    public Set<String> getIncrementTags(Set<String> tags) {
        return showIncrements && showIncrementTags
                ? tags
                : Collections.emptySet();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("showIncrements", showIncrements)
                .add("showIncrementTags", showIncrementTags)
                .add("showIncrementDescription", showIncrementDescription)
                .toString();
    }

    public static class ShareInformationBuilder {
        private boolean showIncrements;
        private boolean showIncrementTags;
        private boolean showIncrementDescription;

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

        public ShareInformationBuilder showIncrementDescription(boolean showIncrementDescription) {
            this.showIncrementDescription = showIncrementDescription;
            return this;
        }

        public ShareInformation build() {
            return new ShareInformation(showIncrements, showIncrementTags, showIncrementDescription);
        }
    }
}
