package loans.direct.history;

import loans.direct.Operation;
import loans.direct.users.UserDetails;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
class HistoryQuery {

    private UserDetails names;
    private Collection<Operation> operations;

    Stream<OperationDescription> history(AccountKey account, Instant from, Instant to) {
        return Stream.empty();
    }
}
