package de.skuzzle.cmp.counter;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShallowTallySheet {

    String getId();

    String getName();

    String getUserId();

    boolean isAssignableTo(UserId userId);

    int getTotalCount();

    UserId getAssignedUser();

    Optional<String> getAdminKey();

    String getPublicKey();

    LocalDateTime getCreateDateUTC();

    LocalDateTime getLastModifiedDateUTC();
}
