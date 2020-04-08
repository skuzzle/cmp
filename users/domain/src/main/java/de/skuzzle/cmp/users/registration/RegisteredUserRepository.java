package de.skuzzle.cmp.users.registration;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

interface RegisteredUserRepository extends MongoRepository<RegisteredUser, String> {

    Optional<RegisteredUser> findByEmail(String email);

    Optional<RegisteredUser> findByRegistrationConfirmation_confirmationToken_token(String token);

    Optional<RegisteredUser> findByResetPasswordConfirmation_confirmationToken_token(String token);

}
