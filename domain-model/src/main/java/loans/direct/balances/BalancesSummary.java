package loans.direct.balances;

import loans.direct.Money;
import loans.direct.UserId;
import loans.direct.users.UserDetail;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
public class BalancesSummary {
    UserId user;
    Header header;

    List<Entry> credits;
    List<Entry> debits;
    List<Entry> cleared;

    @Value
    public static class Entry {
        UserDetail counterParty;
        Money balance;
        Instant timestamp;
    }

    @Value
    public static class Header {
        Money credits;
        Money debits;
        Money balance;
    }
}
