package de.skuzzle.tally.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class TallyService {

    private static final int PUBLIC_KEY_LENGTH = 8;

    private final TallyRepository repository;
    private final RandomKeyGenerator randomKeyGenerator;
    private final DateTimeProvider dateTimeProvider;

    public TallyService(TallyRepository repository, RandomKeyGenerator randomKeyGenerator,
            DateTimeProvider dateTimeProvider) {
        this.repository = repository;
        this.randomKeyGenerator = randomKeyGenerator;
        this.dateTimeProvider = dateTimeProvider;
    }

    public TallySheet createNewTallySheet(String name) {
        final TallySheet tallySheet = new TallySheet();
        tallySheet.setName(name);
        tallySheet.setIncrements(new ArrayList<>());
        tallySheet.setAdminKey(randomKeyGenerator.generateAdminKey());
        tallySheet.setPublicKey(randomKeyGenerator.generatePublicKey(PUBLIC_KEY_LENGTH));
        return repository.save(tallySheet);
    }

    public TallySheet getTallySheet(String publicKey) {
        final Optional<TallySheet> byPublicKey = repository.findByPublicKey(publicKey);
        if (byPublicKey.isPresent()) {
            final TallySheet publicTallySheet = byPublicKey.get();
            publicTallySheet.setAdminKey(null);
            return publicTallySheet;
        }
        return repository.findByAdminKey(publicKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(publicKey));
    }

    public TallySheet increment(String adminKey, TallyIncrement increment) {
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));

        final LocalDateTime createDate = dateTimeProvider.getNow().map(LocalDateTime::from)
                .orElseThrow(IllegalStateException::new);
        increment.setCreateDateUTC(createDate);

        tallySheet.getIncrements().add(increment);
        return repository.save(tallySheet);
    }

    public void deleteTallySheet(String adminKey) {
        final TallySheet tallySheet = repository.findByAdminKey(adminKey)
                .orElseThrow(() -> new TallySheetNotAvailableException(adminKey));
        repository.delete(tallySheet);
    }

}
