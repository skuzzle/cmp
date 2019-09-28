package de.skuzzle.tally.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TallyService {

    private static final int PUBLIC_KEY_LENGTH = 8;

    private final TallyRepository repository;
    private final RandomKeyGenerator randomKeyGenerator;

    TallyService(TallyRepository repository, RandomKeyGenerator randomKeyGenerator) {
        this.repository = repository;
        this.randomKeyGenerator = randomKeyGenerator;
    }

    public TallySheet createNewTallySheet(String name) {
        return repository.save(TallySheet.newTallySheet(
                name,
                randomKeyGenerator.generateAdminKey(),
                randomKeyGenerator.generatePublicKey(PUBLIC_KEY_LENGTH)));
    }

    public TallySheet getTallySheet(String publicKey) {
        final Optional<TallySheet> byPublicKey = repository.findByPublicKey(publicKey);
        if (byPublicKey.isPresent()) {
            final TallySheet publicTallySheet = byPublicKey.get();
            return publicTallySheet.withWipedAdminKey();
        }
        return repository.findByAdminKey(publicKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(publicKey));
    }

    public TallySheet increment(String adminKey, TallyIncrement increment) {
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        tallySheet.incrementWith(increment);
        return repository.save(tallySheet);
    }

    public void deleteTallySheet(String adminKey) {
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));
        repository.delete(tallySheet);
    }

    public void deleteIncrement(String adminKey, String incrementId) {
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));
        if (tallySheet.deleteIncrementWithId(incrementId)) {
            repository.save(tallySheet);
        } else {
            throw new IncrementNotAvailableException(incrementId);
        }
    }

}
