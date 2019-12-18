package loans.direct.history;

import loans.direct.Operation;
import loans.direct.UserId;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
class OperationTitleMapping {

    private final UserId user;
    private final String counterPartyName;

    private static final Map<Operation.Kind, String> fromMe = Map.of(
            Operation.Kind.LOAN, "%s pożyczył od Ciebie kwotę %s PLN, saldo po operacji %s PLN",
            Operation.Kind.PAYBACK, "%s otrzymał od Ciebie kwotę %s PLN, saldo po operacji %s PLN"
    );
    private static final Map<Operation.Kind, String> toMe = Map.of(
            Operation.Kind.PAYBACK, "%s zwrócił Ci kwotę %s PLN, saldo po operacji %s PLN",
            Operation.Kind.LOAN, "%s pożyczył Ci kwotę %s PLN, saldo po operacji %s PLN"
    );

    String map(Operation operation) {
        boolean fromMe = operation.getDetails().getFrom().equals(user);
        Operation.Kind kind = operation.getType();
        String template = fromMe ? this.fromMe.get(kind) : this.toMe.get(kind);
        String operationTitle = String.format(template,
                counterPartyName,
                operation.getDetails().getAmount(),
                operation.getBalance().of(user)
        );
        return operationTitle;
    }
}
