package de.skuzzle.tally.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import io.micrometer.core.instrument.Metrics;

@Service
public class TallyService {

    private static final int PUBLIC_KEY_LENGTH = 8;

    private final TallyRepository repository;
    private final RandomKeyGenerator randomKeyGenerator;

    TallyService(TallyRepository repository, RandomKeyGenerator randomKeyGenerator) {
        this.repository = repository;
        this.randomKeyGenerator = randomKeyGenerator;
    }

    public int countAllTallySheets() {
        return Ints.saturatedCast(repository.count());
    }

    public TallySheet createNewTallySheet(UserId user, String name) {
        Metrics.counter("created_tally", "user_id", user.getMetricsId()).increment();
        return repository.save(TallySheet.newTallySheet(
                user,
                name,
                randomKeyGenerator.generateAdminKey(),
                randomKeyGenerator.generatePublicKey(PUBLIC_KEY_LENGTH)));
    }

    public TallySheet assignToUser(String adminKey, UserId userId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(userId != null, "userId must not be null");
        final TallySheet byAdminKey = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        Metrics.counter("assigned_tally", "user_id", userId.getMetricsId()).increment();
        byAdminKey.assignToUser(userId);
        return repository.save(byAdminKey);
    }

    public List<ShallowTallySheet> getTallySheetsFor(UserId userId) {
        Preconditions.checkArgument(userId != null, "userId must not be null");
        return repository.findByUserId(userId.toString());
    }

    public TallySheet getTallySheet(String publicKey) {
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");
        final Optional<TallySheet> byPublicKey = repository.findByPublicKey(publicKey);
        if (byPublicKey.isPresent()) {
            final TallySheet publicTallySheet = byPublicKey.get();
            return publicTallySheet.withWipedAdminKey();
        }
        return repository.findByAdminKey(publicKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(publicKey));
    }

    public TallySheet increment(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        Metrics.counter("incremented_tally", "user_id", tallySheet.getAssignedUser().getMetricsId()).increment();
        tallySheet.incrementWith(increment);
        return repository.save(tallySheet);
    }

    public TallySheet updateIncrement(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        tallySheet.updateIncrement(increment);
        return repository.save(tallySheet);
    }

    public TallySheet changeName(String adminKey, String newName) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));
        tallySheet.changeNameTo(newName);
        return repository.save(tallySheet);
    }

    public void deleteTallySheet(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));
        repository.delete(tallySheet);
    }

    public void deleteIncrement(String adminKey, String incrementId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));
        if (tallySheet.deleteIncrementWithId(incrementId)) {
            repository.save(tallySheet);
        } else {
            throw new IncrementNotAvailableException(incrementId);
        }
    }

}
