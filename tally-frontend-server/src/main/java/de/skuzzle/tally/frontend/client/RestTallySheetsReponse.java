package de.skuzzle.tally.frontend.client;

import java.util.List;

public class RestTallySheetsReponse {

    private final List<RestTallySheet> tallySheets;

    RestTallySheetsReponse(List<RestTallySheet> tallySheets) {
        this.tallySheets = tallySheets;
    }

    public List<RestTallySheet> getTallySheets() {
        return tallySheets;
    }
}
