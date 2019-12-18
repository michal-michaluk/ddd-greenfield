package loans.direct.history

import loans.direct.*
import loans.direct.users.UserDetail
import loans.direct.users.UserDetails

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

trait OperationsTesting {

    Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    UserId exampleUser() {
        new UserId(2)
    }

    UserId exampleCounterParty() {
        new UserId(1)
    }

    UserId anotherUser3() {
        new UserId(3)
    }

    UserId anotherUser4() {
        new UserId(4)
    }

    UserId anotherUser5() {
        new UserId(5)
    }

    UserId anotherUser6() {
        new UserId(6)
    }

    UserId anotherUser7() {
        new UserId(7)
    }

    UserId anotherUser8() {
        new UserId(8)
    }

    Operation loan(UserId from, UserId to, Money amount, Money balance) {
        Operation.loan(details(from, to, amount), new Balance(from, to, balance))
    }

    Operation payback(UserId from, UserId to, Money amount, Money balance) {
        Operation.payback(details(from, to, amount), new Balance(from, to, balance))
    }

    UserDetails userDetails() {
        def users = [
                (exampleUser())        : new UserDetail(exampleUser(), "Michał", "img/empty.jpg"),
                (exampleCounterParty()): new UserDetail(exampleCounterParty(), "Maciek", "img/empty.jpg"),
                (anotherUser3())       : new UserDetail(anotherUser3(), "Maciek", "img/empty.jpg"),
                (anotherUser4())       : new UserDetail(anotherUser4(), "Krzysztof", "img/empty.jpg"),
                (anotherUser5())       : new UserDetail(anotherUser5(), "Anna", "img/empty.jpg"),
                (anotherUser6())       : new UserDetail(anotherUser6(), "Daria", "img/empty.jpg"),
                (anotherUser7())       : new UserDetail(anotherUser7(), "Paweł", "img/empty.jpg"),
                (anotherUser8())       : new UserDetail(anotherUser8(), "Paweł", "img/empty.jpg"),
        ]

        return { user -> users[user] }
    }

    String exampleCounterPartyName() {
        "Maciek"
    }

    TransactionDetails details(UserId creditor, UserId debtor, Money amount) {
        new TransactionDetails(Instant.now(), creditor, debtor, amount)
    }

    Money money(long amount) {
        Money.of(amount)
    }

    Money balance(Money money) {
        money
    }
}
