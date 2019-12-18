package loans.direct.history;

import loans.direct.TransactionDetails;
import loans.direct.UserId;
import lombok.Value;

@Value
public class AccountKey {
    private UserId user;
    private UserId counterParty;

    AccountKey(UserId user, UserId counterParty) {
        this.user = user;
        this.counterParty = counterParty;
    }

    static AccountKey of(TransactionDetails transaction) {
        return new AccountKey(transaction.getFrom(), transaction.getTo());
    }

    public boolean contains(UserId user) {
        return user.equals(this.user) || user.equals(counterParty);
    }
}
