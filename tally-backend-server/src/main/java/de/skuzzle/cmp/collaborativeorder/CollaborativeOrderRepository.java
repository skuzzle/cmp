package de.skuzzle.cmp.collaborativeorder;

import org.springframework.data.mongodb.repository.MongoRepository;

interface CollaborativeOrderRepository extends MongoRepository<CollaborativeOrder, String> {

}
