package de.skuzzle.cmp.counter.domain;

import java.util.List;

public class RestTallySheetsReponse {

    private final List<RestTallySheet> tallySheets;
    
    private RestTallySheetsReponse(List<RestTallySheet> tallySheets, RestErrorMessage errorMessage) {
        this.tallySheets = tallySheets;
    }
    
    public static RestTallySheetsReponse of(List<RestTallySheet> tallySheets) {
        return new RestTallySheetsReponse(tallySheets, null);
    }
    public List<RestTallySheet> getTallySheets() {
        return tallySheets;
    }
}
