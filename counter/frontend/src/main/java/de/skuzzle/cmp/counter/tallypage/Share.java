package de.skuzzle.cmp.counter.tallypage;

import java.util.List;
import java.util.stream.Collectors;

import de.skuzzle.cmp.counter.client.RestShareDefinition;
import de.skuzzle.cmp.counter.client.RestTallySheet;

public class Share {

    private final boolean canBeDeleted;

    private final String shareId;
    private final boolean showIncrements;
    private final boolean showIncrementTags;
    private final boolean showIncrementDescription;

    private Share(boolean canBeDeleted, String shareId, boolean showIncrements, boolean showIncrementTags,
            boolean showIncrementDescription) {
        this.shareId = shareId;
        this.canBeDeleted = canBeDeleted;
        this.showIncrements = showIncrements;
        this.showIncrementTags = showIncrementTags;
        this.showIncrementDescription = showIncrementDescription;
    }

    public static List<Share> fromBackendResponse(RestTallySheet tallySheet) {
        return tallySheet.getShareDefinitions().stream()
                .map(shareDefinition -> {
                    final boolean canBeDeleted = !shareDefinition.equals(tallySheet.getDefaultShareDefinition());
                    return fromBackendResponse(canBeDeleted, shareDefinition);
                })
                .collect(Collectors.toList());
    }

    private static Share fromBackendResponse(boolean canBeDeleted, RestShareDefinition shareDefinition) {
        return new Share(canBeDeleted, shareDefinition.getShareId(),
                shareDefinition.getShareInformation().isShowIncrements(),
                shareDefinition.getShareInformation().isShowIncrementTags(),
                shareDefinition.getShareInformation().isShowIncrementDescription());
    }

    public boolean canBeDeleted() {
        return this.canBeDeleted;
    }

    public String getShareId() {
        return this.shareId;
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
