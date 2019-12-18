package loans.direct.history;

import loans.direct.Operation;
import loans.direct.UserId;
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
        UserId user = account.getUser();
        UserId counterParty = account.getCounterParty();
        OperationTitleMapping title = new OperationTitleMapping(user, names.get(counterParty).getName());
        return operations.stream()
                .dropWhile(operation -> operation.getDetails().getTimestamp().isBefore(from))
                .takeWhile(operation -> operation.getDetails().getTimestamp().isBefore(to))
                .filter(operation -> account.contains(user))
                .map(operation -> new OperationDescription(
                        operation.getDetails().getTimestamp(),
                        title.map(operation))
                );
    }
}
