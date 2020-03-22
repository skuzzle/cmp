package de.skuzzle.cmp.counter.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RestShareDefinition {

    private final String shareId;
    private final RestShareInformation shareInformation;

    private RestShareDefinition(String shareId, RestShareInformation shareInformation) {
        this.shareId = shareId;
        this.shareInformation = shareInformation;
    }

    public static RestShareDefinition fromDomainObject(ShareDefinition shareDefinition) {
        return new RestShareDefinition(
                shareDefinition.getShareId(),
                RestShareInformation.fromDomainObject(shareDefinition.getShareInformation()));
    }

    public static List<RestShareDefinition> fromDomainObjects(Collection<ShareDefinition> shareDefinitions) {
        return shareDefinitions.stream()
                .map(RestShareDefinition::fromDomainObject)
                .collect(Collectors.toList());
    }

    public String getShareId() {
        return this.shareId;
    }

    public RestShareInformation getShareInformation() {
        return this.shareInformation;
    }
}
