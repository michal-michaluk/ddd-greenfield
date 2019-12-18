package loans.direct.account;

import loans.direct.Event;
import loans.direct.Money;
import loans.direct.Operation;
import loans.direct.UserId;
import loans.direct.persistence.EventEntity;
import loans.direct.persistence.ObjectSerialisation;
import lombok.Value;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.ResultSetIterable;
import org.sql2o.Sql2o;

import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Singleton
public class AccountEventSourcingRepository implements AccountRepository {

    private static final String OPERATION = "Operation";
    private final Sql2o sql;
    private final ObjectSerialisation serialisation;

    public AccountEventSourcingRepository(Sql2o sql, ObjectSerialisation serialisation) {
        this.sql = sql;
        this.serialisation = serialisation;
    }

    @Override
    public Account get(AccountKey key) {
        Optional<EventEntity> lastOperation = findLastOperation(key);
        AccountId id = lastOperation.map(this::toId).orElseGet(() -> initAccount(key));
        return lastOperation
                .map(e -> (Operation) serialisation.fromJson(e.getPayload()))
                .map(o -> new Account(
                        id,
                        o.getDetails().getFrom(),
                        o.getDetails().getTo(),
                        o.getBalance().of(o.getDetails().getFrom()),
                        new ArrayList<>()
                ))
                .orElseGet(() -> new Account(id, key.getFrom(), key.getTo(), Money.zero(), new ArrayList<>()));
    }

    private AccountId toId(EventEntity entity) {
        return new AccountId(entity.getObject_id(), entity.getVersion());
    }

    @Override
    public void save(Account account) {
        saveEvents(account.id, account.events);
    }

    @Value
    public static class Snapshot {
        AccountId id;
        UserId from;
        UserId to;
        Money balance;
    }

    Optional<EventEntity> findLastOperation(AccountKey key) {
        try (Connection connection = sql.open();
             Query query = connection.createQuery(
                     "SELECT e.id, e.object_id, a.version, e.type, e.payload" +
                             " FROM loans_direct.events e LEFT JOIN loans_direct.accounts a ON a.object_id = e.object_id" +
                             " WHERE e.type = :type AND (a.user1 in (:users) OR a.user2 in (:users)) ORDER BY e.id DESC LIMIT 1");
             ResultSetIterable<EventEntity> result = query
                     .addParameter("type", OPERATION)
                     .addParameter("users", key.asSet())
                     .executeAndFetchLazy(EventEntity.class)) {
            return StreamSupport.stream(result.spliterator(), false)
                    .findFirst();
        }
    }

    void saveEvents(AccountId id, List<Event> events) {
        try (Connection connection = sql.beginTransaction()) {
            int result = connection.createQuery(
                    "UPDATE loans_direct.accounts SET version = version + 1" +
                            " WHERE version = :version AND object_id = :object_id")
                    .addParameter("object_id", id.getId())
                    .addParameter("version", id.getVersion())
                    .executeUpdate()
                    .getResult();
            if (result != 1) {
                throw new IllegalStateException();
            }
            events.forEach(event -> connection.createQuery(
                    "INSERT INTO loans_direct.events(emitted, object_id, type, payload) " +
                            " VALUES (:emitted, :object_id, :type, CAST(:payload AS json))")
                    .addParameter("emitted", Timestamp.from(Instant.now()))
                    .addParameter("object_id", id.getId())
                    .addParameter("type", OPERATION)
                    .addParameter("payload", serialisation.toJson(event))
                    .executeUpdate());
            connection.commit();
        }
    }

    AccountId initAccount(AccountKey key) {
        try (Connection connection = sql.beginTransaction()) {
            Long id = connection.createQuery(
                    "INSERT INTO loans_direct.accounts(version, user1, user2) " +
                            " VALUES (:version, :user1, :user2)", true)
                    .addParameter("version", 0L)
                    .addParameter("user1", key.getFrom().getId())
                    .addParameter("user2", key.getTo().getId())
                    .executeUpdate()
                    .getKey(Long.class);
            connection.commit();
            return new AccountId(id, 0);
        }
    }
}
