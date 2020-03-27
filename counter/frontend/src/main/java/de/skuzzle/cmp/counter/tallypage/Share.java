package de.skuzzle.cmp.counter.tallypage;

import java.util.List;
import java.util.stream.Collectors;

import de.skuzzle.cmp.counter.client.RestShareDefinition;
import de.skuzzle.cmp.counter.client.RestTallySheet;

public class Share {

    private final String shareId;
    private final boolean showIncrements;
    private final boolean showIncrementTags;
    private final boolean showIncrementDescription;

    private Share(String shareId, boolean showIncrements, boolean showIncrementTags,
            boolean showIncrementDescription) {
        this.shareId = shareId;
        this.showIncrements = showIncrements;
        this.showIncrementTags = showIncrementTags;
        this.showIncrementDescription = showIncrementDescription;
    }

    public static List<Share> fromBackendResponse(RestTallySheet tallySheet) {
        return tallySheet.getShareDefinitions().stream()
                .map(shareDefinition -> fromBackendResponse(shareDefinition))
                .collect(Collectors.toList());
    }

    private static Share fromBackendResponse(RestShareDefinition shareDefinition) {
        return new Share(shareDefinition.getShareId(),
                shareDefinition.getShareInformation().isShowIncrements(),
                shareDefinition.getShareInformation().isShowIncrementTags(),
                shareDefinition.getShareInformation().isShowIncrementDescription());
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
