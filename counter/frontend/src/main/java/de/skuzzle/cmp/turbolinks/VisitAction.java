package de.skuzzle.cmp.turbolinks;

public enum VisitAction {
    ADVANCE("advance"),
    REPLACE("replace");

    private final String turbolinksAction;

    VisitAction(String turbolinksAction) {
        this.turbolinksAction = turbolinksAction;
    }

    public String toTurbolinksJsonString() {
        return String.format("{ action: \"%s\" }", turbolinksAction);
    }

    @Override
    public String toString() {
        return turbolinksAction;
    }
}
