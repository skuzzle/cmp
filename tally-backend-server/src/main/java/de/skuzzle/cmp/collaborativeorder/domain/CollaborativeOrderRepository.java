package de.skuzzle.cmp.collaborativeorder.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

interface CollaborativeOrderRepository extends MongoRepository<CollaborativeOrder, String> {

}
