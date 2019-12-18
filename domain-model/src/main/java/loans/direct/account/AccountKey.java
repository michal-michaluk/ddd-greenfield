package loans.direct.account;

import loans.direct.TransactionDetails;
import loans.direct.UserId;

import java.util.Objects;
import java.util.Set;

class AccountKey {
    private UserId from;
    private UserId to;

    AccountKey(UserId from, UserId to) {
        this.from = from;
        this.to = to;
    }

    static AccountKey of(TransactionDetails transaction) {
        return new AccountKey(transaction.getFrom(), transaction.getTo());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountKey key = (AccountKey) o;
        return directionEquals(key.from, key.to)
                || directionEquals(key.to, key.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to) + Objects.hash(to, from);
    }

    private boolean directionEquals(UserId user1, UserId user2) {
        return Objects.equals(this.from, user1) &&
                Objects.equals(this.to, user2);
    }

    public Set<Long> asSet() {
        return Set.of(from.getId(), to.getId());
    }

    UserId getFrom() {
        return from;
    }

    UserId getTo() {
        return to;
    }
}
