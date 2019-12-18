package loans.direct.account;

public interface AccountRepository {
    Account get(AccountKey key);

    void save(Account account);
}
