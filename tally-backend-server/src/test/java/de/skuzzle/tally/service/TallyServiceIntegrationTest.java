package de.skuzzle.tally.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import com.google.common.collect.ImmutableSet;

@SpringBootTest
public class TallyServiceIntegrationTest {

    @Autowired
    private TallyService tallyService;

    @Test
    void testCreateTallySheet() throws Exception {
        final TallySheet sheet = tallyService.createNewTallySheet("test");
        assertSoftly(softly -> {
            softly.assertThat(sheet.getIncrements()).isEmpty();
            softly.assertThat(sheet.getAdminKey()).isNotEmpty();
            softly.assertThat(sheet.getName()).isEqualTo("test");
            softly.assertThat(sheet.getPublicKey()).isNotEmpty();
            softly.assertThat(sheet.getLastModifiedDateUTC()).isNotNull();
            softly.assertThat(sheet.getCreateDateUTC()).isNotNull();
        });
    }

    @Test
    void testGetTallySheetByAdminKey() throws Exception {
        final TallySheet sheet = tallyService.createNewTallySheet("test");
        final TallySheet tallySheet = tallyService.getTallySheet(sheet.getAdminKey());
        assertThat(tallySheet.getName()).isEqualTo("test");
        assertThat(tallySheet.getAdminKey()).isEqualTo(sheet.getAdminKey());
    }

    @Test
    void testGetTallySheetByPublicKey() throws Exception {
        final TallySheet sheet = tallyService.createNewTallySheet("test");
        final TallySheet tallySheet = tallyService.getTallySheet(sheet.getPublicKey());
        assertThat(tallySheet.getName()).isEqualTo("test");
        assertThat(tallySheet.getAdminKey()).isNull();
    }

    @Test
    void testGetTallySheetUnknown() throws Exception {
        assertThatExceptionOfType(TallySheetNotAvailableException.class).isThrownBy(() -> tallyService.getTallySheet("1234"));
    }

    @Test
    void testIncrementWithPublicKey() throws Exception {
        final TallyIncrement validIncrement = new TallyIncrement();
        validIncrement.setTags(ImmutableSet.of("pizza"));
        validIncrement.setDescription("test");
        validIncrement.setCreateDateUTC(LocalDateTime.now());

        final TallySheet tallySheet = tallyService.createNewTallySheet("increment");
        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.increment(tallySheet.getPublicKey(), validIncrement));
    }

    @Test
    void testDeleteUnknownAdminKey() throws Exception {
        assertThatExceptionOfType(TallySheetNotAvailableException.class).isThrownBy(() -> tallyService.deleteTallySheet("1234"));
    }

    @Test
    void testDeleteWithPublicKey() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet("deleteMe");
        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.deleteTallySheet(tallySheet.getPublicKey()));
    }

    @Test
    void testDeleteWithAdminKey() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet("deleteMe");
        tallyService.deleteTallySheet(tallySheet.getAdminKey());
        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.getTallySheet(tallySheet.getPublicKey()));
    }

    @Test
    void testIncrementUnknownAdminKey() throws Exception {
        final TallyIncrement validIncrement = new TallyIncrement();
        validIncrement.setTags(ImmutableSet.of("pizza"));
        validIncrement.setDescription("test");
        validIncrement.setCreateDateUTC(LocalDateTime.now());

        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.increment("1234", validIncrement));
    }

    @Test
    void testIncrement() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet("incrementMe");
        final TallyIncrement increment = new TallyIncrement();
        increment.setTags(ImmutableSet.of("pizza"));
        increment.setDescription("test");

        final TallySheet updated = tallyService.increment(tallySheet.getAdminKey(), increment);

        assertSoftly(softly -> {
            softly.assertThat(updated.getVersion()).isNotEqualTo(tallySheet.getVersion());
            softly.assertThat(updated.getIncrements()).hasSize(1);
            softly.assertThat(updated.getIncrements()).first().extracting(TallyIncrement::getCreateDateUTC).isNotNull();
        });
    }
}
