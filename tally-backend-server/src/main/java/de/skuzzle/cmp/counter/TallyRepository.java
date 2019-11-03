package de.skuzzle.cmp.counter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

interface TallyRepository extends MongoRepository<TallySheet, String> {
    
    List<ShallowTallySheet> findByUserId(String userId);

    Optional<TallySheet> findByAdminKey(String adminKey);

    Optional<TallySheet> findByPublicKey(String publicKey);
}
