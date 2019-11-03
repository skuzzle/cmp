package de.skuzzle.cmp.frontend.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RestTallyMetaInfoResponse {

    private final int totalTallySheetCount;

    @JsonCreator
    RestTallyMetaInfoResponse(@JsonProperty("totalTallySheetCount") int totalTallySheetCount) {
        this.totalTallySheetCount = totalTallySheetCount;
    }

    public static RestTallyMetaInfoResponse of(int totalTallySheetCount) {
        return new RestTallyMetaInfoResponse(totalTallySheetCount);
    }

    public int getTotalTallySheetCount() {
        return this.totalTallySheetCount;
    }
}
