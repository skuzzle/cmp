package de.skuzzle.cmp.counter.domain;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import io.micrometer.core.instrument.Metrics;

@Service
public class TallyService {

    private static final Logger log = LoggerFactory.getLogger(TallyService.class);

    private final TallyRepository tallyRepository;

    TallyService(TallyRepository tallyRepository) {
        this.tallyRepository = tallyRepository;
    }

    public int countAllTallySheets() {
        return Ints.saturatedCast(tallyRepository.count());
    }

    public TallySheet createNewTallySheet(UserId user, String name) {
        Metrics.counter("created_tally", "user_id", user.getMetricsId()).increment();
        log.info("User {} created counter named {}", user.getMetricsId(), name);

        return tallyRepository.save(TallySheet.newTallySheetWithRandomAdminKey(
                user,
                name));
    }

    public TallySheet assignToUser(String adminKey, UserId userId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(userId != null, "userId must not be null");
        final TallySheet byAdminKey = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        Metrics.counter("assigned_tally", "user_id", userId.getMetricsId()).increment();
        log.info("Assigned existing counter to user {}", userId.getMetricsId());
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

        final ShareDefinition shareDefinition = tallySheet.share(ShareDefinition.withRandomId(shareInformation));

        // this should really not happen but better be safe here
        ensureShareDoesntExist(shareDefinition.getShareId());

        final UserId user = tallySheet.getAssignedUser();
        Metrics.counter("share_added", "user_id", user.getMetricsId()).increment();
        log.info("User {} added a new share to counter named {}", user.getMetricsId(), tallySheet.getName());

        return tallyRepository.save(tallySheet);
    }

    private void ensureShareDoesntExist(String shareId) {
        // this should really not happen but better be safe here
        final Optional<TallySheet> existing = tallyRepository.findByShareDefinitions_shareId(shareId);
        Preconditions.checkState(existing.isEmpty(), "Cant add share to counter: public key collision!");
    }

    public TallySheet deleteShare(String adminKey, String shareId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(shareId != null, "shareId must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        tallySheet.unshare(shareId);

        final UserId user = tallySheet.getAssignedUser();
        Metrics.counter("share_deleted", "user_id", user.getMetricsId()).increment();
        log.info("User {} deleted share from counter named {}", user.getMetricsId(), tallySheet.getName());

        return tallyRepository.save(tallySheet);
    }

    public TallySheet increment(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        tallySheet.incrementWith(increment);

        final UserId user = tallySheet.getAssignedUser();
        Metrics.counter("incremented_tally", "user_id", user.getMetricsId()).increment();
        log.info("User {} incremented the counter named {}. New Total count: {}", user.getMetricsId(),
                tallySheet.getName(), tallySheet.getTotalCount());

        return tallyRepository.save(tallySheet);
    }

    public TallySheet updateIncrement(String adminKey, TallyIncrement increment) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        tallySheet.updateIncrement(increment);

        final UserId user = tallySheet.getAssignedUser();
        Metrics.counter("updated_increment", "user_id", user.getMetricsId()).increment();
        log.info("User {} updated an increment for counter named {}", user.getMetricsId(), tallySheet.getName());

        return tallyRepository.save(tallySheet);
    }

    public TallySheet changeName(String adminKey, String newName) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        final String previousName = tallySheet.getName();
        tallySheet.changeNameTo(newName);

        final UserId user = tallySheet.getAssignedUser();
        Metrics.counter("changed_name", "user_id", user.getMetricsId()).increment();
        log.info("User {} changed the name of counter {} to {}", user.getMetricsId(), previousName, newName);

        return tallyRepository.save(tallySheet);
    }

    public void deleteTallySheet(String adminKey) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        final UserId user = tallySheet.getAssignedUser();
        Metrics.counter("deleted_tally", "user_id", user.getMetricsId()).increment();
        log.info("User {} deleted the counter named {}", user.getMetricsId(), tallySheet.getName());

        tallyRepository.delete(tallySheet);
    }

    public void deleteIncrement(String adminKey, String incrementId) {
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        final TallySheet tallySheet = tallyRepository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        if (tallySheet.deleteIncrementWithId(incrementId)) {
            final UserId user = tallySheet.getAssignedUser();
            Metrics.counter("delete_increment", "user_id", user.getMetricsId()).increment();
            log.info("User {} deleted an increment for counter named {}. New total count: {}", user.getMetricsId(),
                    tallySheet.getName(), tallySheet.getTotalCount());

            tallyRepository.save(tallySheet);
        } else {
            throw new IncrementNotAvailableException(incrementId);
        }
    }

}
