package de.skuzzle.cmp.counter.timeline;

import java.util.List;

import com.google.common.base.Preconditions;

public class Timeline {

    private final List<TimelineYear> years;
    private final String adminKey;

    private Timeline(List<TimelineYear> years, String adminKey) {
        this.years = years;
        this.adminKey = adminKey;
    }

    static Timeline forReadOnly(List<TimelineYear> years) {
        Preconditions.checkArgument(years != null, "years must not be null");
        return new Timeline(years, null);
    }

    static Timeline forAdmin(List<TimelineYear> years, String adminKey) {
        Preconditions.checkArgument(years != null, "years must not be null");
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        return new Timeline(years, adminKey);
    }

    public boolean isAdmin() {
        return this.adminKey != null;
    }

    public String getAdminKey() {
        Preconditions.checkState(this.isAdmin(), "No admin key available");
        return this.adminKey;
    }

    public List<TimelineYear> getYears() {
        return this.years;
    }
}
