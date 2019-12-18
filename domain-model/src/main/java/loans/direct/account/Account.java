package loans.direct.account;

import loans.direct.*;

import java.util.List;
import java.util.Set;

class Account {

    final AccountId id;
    private final UserId from;
    private final UserId to;
    private Money balance;

    final List<Event> events;

    Account(AccountId id, UserId from, UserId to, Money balance, List<Event> events) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.balance = balance;
        this.events = events;
    }

    Operation loan(TransactionDetails transaction) {
        Balance balance = process(transaction);
        Operation operation = Operation.loan(transaction, balance);
        events.add(operation);
        return operation;
    }

    Operation payback(TransactionDetails transaction) {
        Balance balance = process(transaction);
        Operation operation = Operation.payback(transaction, balance);
        events.add(operation);
        return operation;
    }

    private Balance process(TransactionDetails transaction) {
        rightAccountForUsers(transaction);
        if (from.equals(transaction.getFrom())) {
            balance = balance.add(transaction.getAmount());
        } else {
            balance = balance.subtract(transaction.getAmount());
        }
        return balance();
    }

    Balance balance() {
        return new Balance(
                this.from,
                this.to,
                this.balance
        );
    }

    private void rightAccountForUsers(TransactionDetails transaction) {
        if (!Set.of(from, to).containsAll(Set.of(transaction.getFrom(), transaction.getTo()))) {
            throw new IllegalArgumentException();
        }
    }
}
