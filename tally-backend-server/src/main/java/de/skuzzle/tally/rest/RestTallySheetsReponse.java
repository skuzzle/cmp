package de.skuzzle.tally.rest;

import java.util.List;

public class RestTallySheetsReponse {

    private final List<RestTallySheet> tallySheets;
    private final RestErrorMessage errorMessage;
    
    private RestTallySheetsReponse(List<RestTallySheet> tallySheets, RestErrorMessage errorMessage) {
        this.tallySheets = tallySheets;
        this.errorMessage = errorMessage;
    }
    
    public static RestTallySheetsReponse of(List<RestTallySheet> tallySheets) {
        return new RestTallySheetsReponse(tallySheets, null);
    }
    
    public static RestTallySheetsReponse failure(String message, String origin) {
        return new RestTallySheetsReponse(null, new RestErrorMessage(message, origin));
    }
    
    public List<RestTallySheet> getTallySheets() {
        return tallySheets;
    }
    
    public RestErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
