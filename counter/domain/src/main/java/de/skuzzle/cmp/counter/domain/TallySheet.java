package de.skuzzle.cmp.counter.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.Preconditions;

import de.skuzzle.cmp.common.collections.Lists;

@Document
public class TallySheet implements ShallowTallySheet {

    @Id
    private String id;
    @Version
    private int version;

    @Indexed
    private String userId;

    @Transient
    private UserId assignedUser;
    @Transient
    private int totalCount;

    private String name;
    @Indexed
    private String adminKey;
    @Indexed
    private final String publicKey;
    private final List<TallyIncrement> increments;
    private final List<ShareDefinition> shareDefinitions;

    // dates in UTC+0
    @CreatedDate
    private LocalDateTime createDateUTC;
    @LastModifiedDate
    private LocalDateTime lastModifiedDateUTC;

    // private c'tor without validation only for spring-data
    private TallySheet(String userId, String name, String adminKey, String publicKey, List<TallyIncrement> increments,
            List<ShareDefinition> shareDefinitions) {
        this.userId = userId;
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
        this.increments = increments;
        this.shareDefinitions = shareDefinitions;
        this.totalCount = increments.size();
        this.assignedUser = UserId.fromLegacyStringId(userId);
    }

    public static TallySheet newTallySheet(UserId userId, String name, String adminKey, String publicKey) {
        Preconditions.checkArgument(userId != null, "userId must not be null");
        Preconditions.checkArgument(name != null, "name must not be null");
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");

        return new TallySheet(userId.toString(), name, adminKey, publicKey, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public String getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    @Override
    public UserId getAssignedUser() {
        return this.assignedUser;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Optional<String> getAdminKey() {
        return Optional.ofNullable(this.adminKey);
    }

    public TallySheet withWipedAdminKey() {
        this.adminKey = null;
        return this;
    }

    public TallySheet wipeForShareDefinitionWithId(String shareId) {
        Preconditions.checkArgument(shareId != null, "shareId must not be null");

        final ShareInformation share = this.shareDefinitions.stream()
                .filter(shareDefinition -> shareId.equals(shareId))
                .map(ShareDefinition::getShareInformation)
                .findFirst()
                .orElseThrow(() -> new ShareNotAvailableException(shareId));

        final TallySheet result = new TallySheet(userId, name, adminKey, publicKey,
                share.getIncrements(increments), shareDefinitions);
        result.id = id;
        result.version = version;
        result.createDateUTC = createDateUTC;
        result.lastModifiedDateUTC = lastModifiedDateUTC;
        result.totalCount = totalCount;
        result.adminKey = null;
        return result;
    }

    @Override
    public String getPublicKey() {
        return this.publicKey;
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    public List<TallyIncrement> getIncrements() {
        return Collections.unmodifiableList(this.increments);
    }

    public IncrementQueryResult selectIncrements(IncrementQuery query) {
        Preconditions.checkArgument(query != null, "query must not be null");
        return query.select(getIncrements());
    }

    @Override
    public LocalDateTime getLastModifiedDateUTC() {
        return this.lastModifiedDateUTC;
    }

    @Override
    public LocalDateTime getCreateDateUTC() {
        return this.createDateUTC;
    }

    @Override
    public boolean isAssignableTo(UserId userId) {
        return this.assignedUser.isAnonymous() && !userId.isAnonymous();
    }

    public void assignToUser(UserId userId) {
        Preconditions.checkArgument(userId != null, "userId must not be null");
        if (!isAssignableTo(userId)) {
            throw new UserAssignmentException(String.format("Sheet with name '%s' is already assigned to: %s",
                    name, assignedUser));
        }

        this.assignedUser = userId;
        this.userId = userId.toString();
    }

    public boolean deleteIncrementWithId(String incrementId) {
        Preconditions.checkArgument(incrementId != null, "incrementId must not be null");
        final boolean removed = this.increments.removeIf(increment -> increment.getId().equals(incrementId));
        this.totalCount = increments.size();
        return removed;
    }

    public void incrementWith(TallyIncrement increment) {
        Preconditions.checkArgument(increment != null, "increment must not be null");
        Lists.firstIndexOf(increments, other -> other.getId().equals(increment.getId()))
                .ifPresent(index -> new IllegalArgumentException(
                        String.format("Increment with id %s already exists in tally sheet with id %s",
                                increment.getId(), getId())));

        this.increments.add(increment);
        this.totalCount = increments.size();
    }

    public void updateIncrement(TallyIncrement increment) {
        Preconditions.checkArgument(increment != null, "increment must not be null");
        final int idx = Lists.firstIndexOf(increments, other -> other.getId().equals(increment.getId()))
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Increment with id %s does not exist in tally sheet with id %s",
                                increment.getId(), getId())));

        this.increments.set(idx, increment);
    }

    public void changeNameTo(String newName) {
        Preconditions.checkArgument(newName != null, "newName must not be null");
        Preconditions.checkArgument(!newName.isEmpty(), "newName must not be empty");
        this.name = newName;
    }

    public List<ShareDefinition> getShareDefinitions() {
        return Collections.unmodifiableList(this.shareDefinitions);
    }

    void share(ShareDefinition shareDefinition) {
        Preconditions.checkArgument(shareDefinition != null, "shareDefinition must not be null");
        this.shareDefinitions.add(shareDefinition);
    }

    void unshare(String shareId) {
        Preconditions.checkArgument(shareId != null, "shareId must not be null");
        final boolean deleted = this.shareDefinitions
                .removeIf(share -> shareId.equals(share.getShareId()));
        if (!deleted) {
            throw new ShareNotAvailableException(shareId);
        }
    }
}
