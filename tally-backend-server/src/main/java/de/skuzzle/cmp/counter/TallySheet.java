package de.skuzzle.cmp.counter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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

    private String name;
    @Indexed
    private String adminKey;
    @Indexed
    private final String publicKey;
    private final List<TallyIncrement> increments;

    // dates in UTC+0
    @CreatedDate
    private LocalDateTime createDateUTC;
    @LastModifiedDate
    private LocalDateTime lastModifiedDateUTC;

    // private c'tor without validation only for spring-data
    private TallySheet(String userId, String name, String adminKey, String publicKey, List<TallyIncrement> increments) {
        this.userId = userId;
        this.name = name;
        this.adminKey = adminKey;
        this.publicKey = publicKey;
        this.increments = increments;
        this.assignedUser = UserId.fromLegacyStringId(userId);
    }

    public static TallySheet newTallySheet(UserId userId, String name, String adminKey, String publicKey) {
        Preconditions.checkArgument(userId != null, "userId must not be null");
        Preconditions.checkArgument(name != null, "name must not be null");
        Preconditions.checkArgument(adminKey != null, "adminKey must not be null");
        Preconditions.checkArgument(publicKey != null, "publicKey must not be null");

        return new TallySheet(userId.toString(), name, adminKey, publicKey, new ArrayList<>());
    }

    @Override
    public String getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    /**
     * @deprecated User {@link #getAssignedUser()}
     */
    @Override
    @Deprecated
    public String getUserId() {
        return this.userId;
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

    @Override
    public String getPublicKey() {
        return this.publicKey;
    }

    @Override
    public int getTotalCount() {
        return increments.size();
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
        return this.increments.removeIf(increment -> increment.getId().equals(incrementId));
    }

    public void incrementWith(TallyIncrement increment) {
        Preconditions.checkArgument(increment != null, "increment must not be null");
        final boolean idExists = firstIndexOf(increments, other -> other.getId().equals(increment.getId())) >= 0;
        Preconditions.checkArgument(!idExists, "Increment with id %s already exists in tally sheet with id %s",
                increment.getId(), getId());

        this.increments.add(increment);
    }

    public void updateIncrement(TallyIncrement increment) {
        Preconditions.checkArgument(increment != null, "increment must not be null");
        final int idx = firstIndexOf(increments, other -> other.getId().equals(increment.getId()));
        final boolean idExists = idx >= 0;
        Preconditions.checkArgument(idExists, "Increment with id %s does not exist in tally sheet with id %s",
                increment.getId(), getId());

        this.increments.set(idx, increment);
    }

    private <T> int firstIndexOf(List<T> list, Predicate<? super T> p) {
        return Lists.firstIndexOf(list, p);
    }

    public void changeNameTo(String newName) {
        Preconditions.checkArgument(newName != null, "newName must not be null");
        Preconditions.checkArgument(!newName.isEmpty(), "newName must not be empty");
        this.name = newName;
    }
}
