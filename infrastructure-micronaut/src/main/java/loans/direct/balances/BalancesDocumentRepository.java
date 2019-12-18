package loans.direct.balances;

import com.fasterxml.jackson.annotation.JsonIgnore;
import loans.direct.UserId;
import loans.direct.persistence.ObjectSerialisation;
import loans.direct.users.UserDetails;
import lombok.Value;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

@Singleton
public class BalancesDocumentRepository {

    private final Sql2o sql;
    private final ObjectSerialisation serialisation;
    private final UserDetails users;

    public BalancesDocumentRepository(Sql2o sql, ObjectSerialisation serialisation, UserDetails users) {
        this.sql = sql;
        this.serialisation = serialisation;
        this.users = users;
    }

    public BalancesState get(UserId user) {
        Optional<EntryPointTable> record = getDocument(user);
        if (record.isPresent()) {
            BalancesState balances = serialisation.fromJson(record.get().getBalances(), BalancesState.class);
            balances.id = record.get().id();
            balances.users = users;
            return balances;
        }
        throw new IllegalArgumentException();
    }

    public void save(BalancesState object) {
        EntryPointTable record = new EntryPointTable(
                object.id.getId(),
                object.id.getVersion(),
                null,
                serialisation.toJson(object)
        );
        saveDocument(record);
    }

    public void init(UserId user) {
        try (Connection connection = sql.beginTransaction()) {
            int result = connection.createQuery(
                    "INSERT INTO loans_direct.entrypoint (version, \"user\", balances)" +
                            " VALUES (0, :user, :balances)")
                    .addParameter("user", user.getId())
                    .addParameter("balances",
                            serialisation.toJson(new BalancesState(null, user, Map.of(), null))
                    )
                    .executeUpdate()
                    .getResult();
            connection.commit();
        }
    }

    private Optional<EntryPointTable> getDocument(UserId user) {
        try (Connection connection = sql.open();
             Query query = connection.createQuery(
                     "SELECT object_id, version, \"user\", balances" +
                             " FROM loans_direct.entrypoint" +
                             " WHERE \"user\" = :user"
             )) {
            EntryPointTable record = query
                    .addParameter("user", user.getId())
                    .executeAndFetchFirst(EntryPointTable.class);

            return Optional.ofNullable(record);
        }
    }

    private void saveDocument(EntryPointTable record) {
        try (Connection connection = sql.beginTransaction()) {
            int result = connection.createQuery(
                    "UPDATE loans_direct.entrypoint" +
                            " SET version = version + 1" +
                            "     balances = :balances" +
                            " WHERE version = :version AND object_id = :object_id")
                    .bind(record)
                    .executeUpdate()
                    .getResult();
            if (result != 1) {
                throw new IllegalStateException();
            }
            connection.commit();
        }
    }

    @Value
    public static class EntryPointTable {
        Long object_id;
        Long version;
        Long user;
        String balances;

        public BalancesId id() {
            return new BalancesId(object_id, version);
        }
    }

    private static class TransientFields {
        @JsonIgnore
        BalancesId id;
        @JsonIgnore
        UserDetails users;
    }
}
