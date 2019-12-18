package loans.direct.account


import loans.direct.Event
import loans.direct.Money
import loans.direct.TransactionDetails
import loans.direct.UserId
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class AccountTest extends Specification {

    Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    def "balance 0, transaction 25"() {
        given:
        def account = accountFor(bob(), anna())

        when:
        def balance = account.process(transaction(bob(), anna(), money(25)))

        then:
        balance.of(bob()) == money(25)
        balance.of(anna()) == money(-25)
    }

    def "balance 0, reverse transaction 25"() {
        given:
        def account = accountFor(bob(), anna())

        when:
        def balance = account.process(transaction(anna(), bob(), money(25)))

        then:
        balance.of(bob()) == money(-25)
        balance.of(anna()) == money(25)
    }

    def "transaction 25, return 25 results balance 0"() {
        given:
        def account = accountFor(bob(), anna())
        account.process(transaction(anna(), bob(), money(25)))

        when:
        def balance = account.process(transaction(bob(), anna(), money(25)))

        then:
        balance.of(bob()) == money(0)
        balance.of(anna()) == money(0)
    }

    def "transaction 25, return 15 results balance 10"() {
        given:
        def account = accountFor(bob(), anna())
        account.process(transaction(anna(), bob(), money(25)))

        when:
        def balance = account.process(transaction(bob(), anna(), money(15)))

        then:
        balance.of(bob()) == money(-10)
        balance.of(anna()) == money(10)
    }

    def "transaction 25, return 35 results balance -10"() {
        given:
        def account = accountFor(bob(), anna())
        account.process(transaction(anna(), bob(), money(25)))

        when:
        def balance = account.process(transaction(bob(), anna(), money(35)))

        then:
        balance.of(bob()) == money(10)
        balance.of(anna()) == money(-10)
    }

    def accountFor(UserId userId1, UserId userId2) {
        new Account(new AccountId(999, 0), userId1, userId2, Money.zero(), new ArrayList<Event>())
    }

    UserId bob() {
        new UserId(1)
    }

    UserId anna() {
        new UserId(2)
    }

    Money money(def amount) {
        Money.of(amount)
    }

    def transaction(UserId from, UserId to, Money amount) {
        new TransactionDetails(Instant.now(clock), from, to, amount)
    }
}
