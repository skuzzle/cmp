package de.skuzzle.tally.frontend.client;

public class RestTallyMetaInfoResponse {

    private final int totalTallySheetCount;

    private RestTallyMetaInfoResponse(int totalTallySheetCount) {
        this.totalTallySheetCount = totalTallySheetCount;
    }

    public static RestTallyMetaInfoResponse of(int totalTallySheetCount) {
        return new RestTallyMetaInfoResponse(totalTallySheetCount);
    }

    public int getTotalTallySheetCount() {
        return this.totalTallySheetCount;
    }
}
