package de.skuzzle.tally.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TallyServiceIntegrationTest {

    private static final UserId USER = UserId.unknown("foo");

    @Autowired
    private TallyService tallyService;

    @Test
    void testCreateTallySheet() throws Exception {
        final TallySheet sheet = tallyService.createNewTallySheet(USER, "test");
        assertSoftly(softly -> {
            softly.assertThat(sheet.getIncrements()).isEmpty();
            softly.assertThat(sheet.getAdminKey()).isNotEmpty();
            softly.assertThat(sheet.getName()).isEqualTo("test");
            softly.assertThat(sheet.getUserId()).isEqualTo(USER.toString());
            softly.assertThat(sheet.getAssignedUser()).isEqualTo(USER);
            softly.assertThat(sheet.getPublicKey()).isNotEmpty();
            softly.assertThat(sheet.getLastModifiedDateUTC()).isNotNull();
            softly.assertThat(sheet.getCreateDateUTC()).isNotNull();
        });
    }

    @Test
    void testGetTallySheetByAdminKey() throws Exception {
        final TallySheet sheet = tallyService.createNewTallySheet(USER, "test");
        final TallySheet tallySheet = tallyService.getTallySheet(sheet.getAdminKey().orElseThrow());
        assertThat(tallySheet.getName()).isEqualTo("test");
        assertThat(tallySheet.getAdminKey()).isEqualTo(sheet.getAdminKey());
    }

    @Test
    void testGetTallySheetByPublicKey() throws Exception {
        final TallySheet sheet = tallyService.createNewTallySheet(USER, "test");
        final TallySheet tallySheet = tallyService.getTallySheet(sheet.getPublicKey());
        assertThat(tallySheet.getName()).isEqualTo("test");
        assertThat(tallySheet.getAdminKey()).isEqualTo(Optional.empty());
    }

    @Test
    void testGetTallySheetUnknown() throws Exception {
        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.getTallySheet("1234"));
    }

    @Test
    void testIncrementWithPublicKey() throws Exception {
        final TallyIncrement validIncrement = TallyIncrement.newIncrement("test", LocalDateTime.now(), Set.of("pizza"));

        final TallySheet tallySheet = tallyService.createNewTallySheet(USER, "increment");
        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.increment(tallySheet.getPublicKey(), validIncrement));
    }

    @Test
    void testDeleteUnknownAdminKey() throws Exception {
        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.deleteTallySheet("1234"));
    }

    @Test
    void testDeleteWithPublicKey() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet(USER, "deleteMe");
        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.deleteTallySheet(tallySheet.getPublicKey()));
    }

    @Test
    void testDeleteWithAdminKey() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet(USER, "deleteMe");
        tallyService.deleteTallySheet(tallySheet.getAdminKey().orElseThrow());
        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.getTallySheet(tallySheet.getPublicKey()));
    }

    @Test
    void testIncrementUnknownAdminKey() throws Exception {
        final TallyIncrement validIncrement = TallyIncrement.newIncrement("test", LocalDateTime.now(), Set.of("pizza"));

        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.increment("1234", validIncrement));
    }

    @Test
    void testIncrement() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet(USER, "incrementMe");
        final TallyIncrement validIncrement = TallyIncrement.newIncrement("test", LocalDateTime.now(), Set.of("pizza"));

        final TallySheet updated = tallyService.increment(tallySheet.getAdminKey().orElseThrow(), validIncrement);

        assertSoftly(softly -> {
            softly.assertThat(updated.getVersion()).isNotEqualTo(tallySheet.getVersion());
            softly.assertThat(updated.getIncrements()).hasSize(1);
            softly.assertThat(updated.getIncrements()).first().extracting(TallyIncrement::getCreateDateUTC).isNotNull();
            softly.assertThat(updated.getIncrements()).first().extracting(TallyIncrement::getId).isNotNull();
        });
    }

    @Test
    void testDeleteIncrementSuccessfully() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet(USER, "incrementMe");
        final TallyIncrement validIncrement = TallyIncrement.newIncrement("test", LocalDateTime.now(), Set.of("pizza"));

        tallyService.increment(tallySheet.getAdminKey().orElseThrow(), validIncrement);
        tallyService.deleteIncrement(tallySheet.getAdminKey().orElseThrow(), validIncrement.getId());

        final TallySheet updated = tallyService.getTallySheet(tallySheet.getAdminKey().orElseThrow());
        assertThat(updated.getIncrements()).isEmpty();
    }

    @Test
    void testDeleteUnknownIncrement() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet(USER, "incrementMe");

        assertThatExceptionOfType(IncrementNotAvailableException.class)
                .isThrownBy(() -> tallyService.deleteIncrement(tallySheet.getAdminKey().orElseThrow(), "foo"));
    }

    @Test
    void testDeleteIncrementWithPublicKey() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet(USER, "incrementMe");
        final TallyIncrement validIncrement = TallyIncrement.newIncrement("test", LocalDateTime.now(), Set.of("pizza"));

        assertThatExceptionOfType(TallySheetNotAvailableException.class)
                .isThrownBy(() -> tallyService.increment(tallySheet.getPublicKey(), validIncrement));
    }

    @Test
    void testRetrieveIncrementedTallySheet() throws Exception {
        final TallySheet tallySheet = tallyService.createNewTallySheet(USER, "incrementMe");
        tallyService.increment(tallySheet.getAdminKey().orElseThrow(),
                TallyIncrement.newIncrement("test", LocalDateTime.now(), Set.of("pizza")));
        tallyService.increment(tallySheet.getAdminKey().orElseThrow(),
                TallyIncrement.newIncrement("test", LocalDateTime.now(), Set.of("pizza")));

        final TallySheet updated = tallyService.getTallySheet(tallySheet.getPublicKey());
        assertThat(updated.getIncrements()).hasSize(2);
    }

    @Test
    void testFindTallySheetsForUserId() throws Exception {
        final UserId user1 = UserId.unknown("user1");
        final UserId user2 = UserId.unknown("user2");

        tallyService.createNewTallySheet(user1, "sheet1");
        tallyService.createNewTallySheet(user1, "sheet2");
        tallyService.createNewTallySheet(user2, "sheet1");

        final List<ShallowTallySheet> sheetsForUser = tallyService.getTallySheetsFor(user1);
        assertThat(sheetsForUser).hasSize(2);
        assertThat(sheetsForUser).first().extracting(ShallowTallySheet::getUserId).isEqualTo(user1.toString());
        assertThat(sheetsForUser).first().extracting(ShallowTallySheet::getAssignedUser).isEqualTo(user1);
        assertThat(sheetsForUser).first().extracting(ShallowTallySheet::getName).isEqualTo("sheet1");
        assertThat(sheetsForUser).first().extracting(ShallowTallySheet::getAdminKey).isNotEqualTo(Optional.empty());
    }

    @Test
    void testAssignToUser() throws Exception {
        final TallySheet sheet = tallyService.createNewTallySheet(UserId.unknown("unknown"), "sheet");
        final UserId authenticated = UserId.wellKnown("google", "foo@gmail.com");

        final TallySheet updatedSheet = tallyService.assignToUser(sheet.getAdminKey().orElseThrow(), authenticated);
        assertThat(updatedSheet.getAssignedUser()).isEqualTo(authenticated);

        // test that sheet can now be found by user lookup
        final List<ShallowTallySheet> sheetsForUser = tallyService.getTallySheetsFor(authenticated);
        assertThat(sheetsForUser).hasSize(1);
    }
}
