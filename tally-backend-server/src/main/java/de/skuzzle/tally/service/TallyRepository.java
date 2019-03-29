package de.skuzzle.tally.service;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

interface TallyRepository extends MongoRepository<TallySheet, String> {

    Optional<TallySheet> findByAdminKey(String adminKey);

    Optional<TallySheet> findByPublicKey(String publicKey);
}
