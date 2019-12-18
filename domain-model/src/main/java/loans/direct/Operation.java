package loans.direct;

import lombok.Value;

@Value
public class Operation implements Event {

    Kind type;
    TransactionDetails details;
    Balance balance;

    public static Operation loan(TransactionDetails details, Balance balance) {
        return new Operation(Kind.LOAN, details, balance);
    }

    public static Operation payback(TransactionDetails details, Balance balance) {
        return new Operation(Kind.PAYBACK, details, balance);
    }

    public enum Kind {
        LOAN, PAYBACK
    }
}
