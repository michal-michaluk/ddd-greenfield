package loans.direct.account;

import loans.direct.TransactionDetails;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public void payback(TransactionDetails transaction) {
        Account account = repository.get(AccountKey.of(transaction));
        account.payback(transaction);
        repository.save(account);
    }

    public void loan(TransactionDetails transaction) {
        Account account = repository.get(AccountKey.of(transaction));
        account.loan(transaction);
        repository.save(account);
    }
}
