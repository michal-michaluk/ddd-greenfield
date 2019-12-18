package loans.direct;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Balance {
    private final UserId from;
    private final UserId to;
    private final Money balance;

    public Money of(UserId side) {
        if (from.equals(side)) {
            return balance;
        } else if (to.equals(side)) {
            return balance.negate();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
