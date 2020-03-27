package de.skuzzle.cmp.counter.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import io.micrometer.core.instrument.Metrics;

@Service
public class TallyService {

    private static final int PUBLIC_KEY_LENGTH = 8;

    private final TallyRepository tallyRepository;
    private final RandomKeyGenerator randomKeyGenerator;

    TallyService(TallyRepository tallyRepository, RandomKeyGenerator randomKeyGenerator) {
        this.tallyRepository = tallyRepository;
        this.randomKeyGenerator = randomKeyGenerator;
    }

    public int countAllTallySheets() {
        return Ints.saturatedCast(tallyRepository.count());
    }

    public TallySheet createNewTallySheet(UserId user, String name) {
        Metrics.counter("created_tally", "user_id", user.getMetricsId()).increment();
        final String adminKey = randomKeyGenerator.generateAdminKey();

        return tallyRepository.save(TallySheet.newTallySheet(
                user,
                name,
                adminKey));
    }

    public TallySheet assignToUser(String adminKey, UserId userId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(userId != null, "userId must not be null");
        final TallySheet byAdminKey = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        Metrics.counter("assigned_tally", "user_id", userId.getMetricsId()).increment();
        byAdminKey.assignToUser(userId);
        return tallyRepository.save(byAdminKey);
    }

    public List<ShallowTallySheet> getTallySheetsFor(UserId userId) {
        Preconditions.checkArgument(userId != null, "userId must not be null");
        return tallyRepository.findByUserId(userId.toString());
    }

    public TallySheet getTallySheet(String publicKey) {
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");

        // this legacy lookup is required to find the sheets that have not been opened
        // since the changes in
        // https://github.com/skuzzle/cmp/issues/49
        final Optional<TallySheet> byPublicKey = tallyRepository.findByPublicKey(publicKey);
        if (byPublicKey.isPresent()) {
            return byPublicKey
                    .map(tallySheet -> tallySheet.wipedCopyForShareDefinitionWithId(publicKey))
                    .orElseThrow();
        }

        final Optional<TallySheet> byAdminKey = tallyRepository.findByAdminKey(publicKey);
        if (byAdminKey.isPresent()) {
            return byAdminKey.orElseThrow();
        }

        return tallyRepository.findByShareDefinitions_shareId(publicKey)
                .map(tallySheet -> tallySheet.wipedCopyForShareDefinitionWithId(publicKey))
                .orElseThrow(() -> new TallySheetNotAvailableException(publicKey));
    }

    public TallySheet addShare(String adminKey, ShareInformation shareInformation) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(shareInformation != null, "shareInformation must not be null");

        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        final String shareId = randomKeyGenerator.generatePublicKey(PUBLIC_KEY_LENGTH);
        tallySheet.share(ShareDefinition.of(shareId, shareInformation));

        // this should really not happen but better be safe here
        final Optional<TallySheet> existing = tallyRepository.findByShareDefinitions_shareId(shareId);
        Preconditions.checkState(existing.isEmpty(), "Cant add share to counter: public key collision!");

        Metrics.counter("share_added", "user_id", tallySheet.getAssignedUser().getMetricsId()).increment();
        return tallyRepository.save(tallySheet);
    }

    public TallySheet deleteShare(String adminKey, String shareId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(shareId != null, "shareId must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        tallySheet.unshare(shareId);

        Metrics.counter("share_deleted", "user_id", tallySheet.getAssignedUser().getMetricsId()).increment();
        return tallyRepository.save(tallySheet);
    }

    public TallySheet increment(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        Metrics.counter("incremented_tally", "user_id", tallySheet.getAssignedUser().getMetricsId()).increment();
        tallySheet.incrementWith(increment);
        return tallyRepository.save(tallySheet);
    }

    public TallySheet updateIncrement(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        Metrics.counter("updated_increment", "user_id", tallySheet.getAssignedUser().getMetricsId()).increment();
        tallySheet.updateIncrement(increment);
        return tallyRepository.save(tallySheet);
    }

    public TallySheet changeName(String adminKey, String newName) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        Metrics.counter("changed_name", "user_id", tallySheet.getAssignedUser().getMetricsId()).increment();
        tallySheet.changeNameTo(newName);
        return tallyRepository.save(tallySheet);
    }

    public void deleteTallySheet(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        Metrics.counter("deleted_tally", "user_id", tallySheet.getAssignedUser().getMetricsId()).increment();
        tallyRepository.delete(tallySheet);
    }

    public void deleteIncrement(String adminKey, String incrementId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        if (tallySheet.deleteIncrementWithId(incrementId)) {
            Metrics.counter("delete_increment", "user_id", tallySheet.getAssignedUser().getMetricsId()).increment();
            tallyRepository.save(tallySheet);
        } else {
            throw new IncrementNotAvailableException(incrementId);
        }
    }

}
