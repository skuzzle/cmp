package de.skuzzle.cmp.counter.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShallowTallySheet {

    String getId();

    String getName();

    boolean isAssignableTo(UserId userId);

    int getTotalCount();

    UserId getAssignedUser();

    Optional<String> getAdminKey();

    String getPublicKey();

    LocalDateTime getCreateDateUTC();

    LocalDateTime getLastModifiedDateUTC();
}
