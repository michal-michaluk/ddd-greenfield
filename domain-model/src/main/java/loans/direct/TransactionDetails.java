package loans.direct;

import lombok.Value;

import java.time.Instant;

@Value
public class TransactionDetails {
    Instant timestamp;
    UserId from;
    UserId to;
    Money amount;

    public UserId getCounterParty(UserId user) {
        if (user.equals(from)) {
            return to;
        } else if (user.equals(to)) {
            return from;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
