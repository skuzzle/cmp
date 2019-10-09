package de.skuzzle.tally.service;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShallowTallySheet {

    String getId();
    
    String getName();

    String getUserId();

    Optional<String> getAdminKey();

    String getPublicKey();

    LocalDateTime getCreateDateUTC();
    
    LocalDateTime getLastModifiedDateUTC();
}
