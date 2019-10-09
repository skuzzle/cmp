package de.skuzzle.tally.frontend.client;

import java.util.List;

public class RestTallySheetsReponse {

    private final List<RestTallySheet> tallySheets;
    
    private RestTallySheetsReponse(List<RestTallySheet> tallySheets, RestErrorMessage errorMessage) {
        this.tallySheets = tallySheets;
    }
    
    public List<RestTallySheet> getTallySheets() {
        return tallySheets;
    }
}
