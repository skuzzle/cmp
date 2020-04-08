package de.skuzzle.cmp.counter.domain;

public class RestShareInformation {

    private final boolean showIncrements;
    private final boolean showIncrementTags;
    private final boolean showIncrementDescription;

    public RestShareInformation(boolean showIncrements, boolean showIncrementTags, boolean showIncrementDescription) {
        this.showIncrements = showIncrements;
        this.showIncrementTags = showIncrementTags;
        this.showIncrementDescription = showIncrementDescription;
    }

    public static RestShareInformation fromDomainObject(ShareInformation shareInformation) {
        return new RestShareInformation(
                shareInformation.isShowIncrements(),
                shareInformation.isShowIncrementTags(),
                shareInformation.isShowIncrementDescription());
    }

    public ShareInformation toDomainObject() {
        return ShareInformation.builder()
                .showIncrements(showIncrements)
                .showIncrementTags(showIncrementTags)
                .showIncrementDescription(showIncrementDescription)
                .build();
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
}
