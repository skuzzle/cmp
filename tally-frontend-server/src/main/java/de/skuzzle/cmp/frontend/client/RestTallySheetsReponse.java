package de.skuzzle.cmp.frontend.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RestTallySheetsReponse {

    private final List<RestTallySheet> tallySheets;

    @JsonCreator
    RestTallySheetsReponse(@JsonProperty("tallySheets") List<RestTallySheet> tallySheets) {
        this.tallySheets = tallySheets;
    }

    public List<RestTallySheet> getTallySheets() {
        return tallySheets;
    }
}
