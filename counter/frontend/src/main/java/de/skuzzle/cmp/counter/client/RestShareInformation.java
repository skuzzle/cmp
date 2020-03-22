package de.skuzzle.cmp.counter.client;

public class RestShareInformation {

    private final boolean showIncrements;
    private final boolean showIncrementTags;
    private final boolean showIncrementDescription;

    RestShareInformation(boolean showIncrements, boolean showIncrementTags, boolean showIncrementDescription) {
        this.showIncrements = showIncrements;
        this.showIncrementTags = showIncrementTags;
        this.showIncrementDescription = showIncrementDescription;
    }

    public boolean isShowIncrements() {
        return this.showIncrements;
    }

    public boolean isShowIncrementTags() {
        return this.showIncrementTags;
    }

    public boolean isShowIncrementDescription() {
        return this.showIncrementDescription;
    }

    public static RestShareInformation create(boolean showIncrements, boolean showIncrementTags,
            boolean showIncrementDescription) {
        return new RestShareInformation(showIncrements, showIncrementTags, showIncrementDescription);
    }
}
