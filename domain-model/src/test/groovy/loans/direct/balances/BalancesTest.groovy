package loans.direct.balances

import loans.direct.Money
import loans.direct.UserId
import loans.direct.history.OperationsTesting
import spock.lang.Specification

import java.time.Instant

class BalancesTest extends Specification implements OperationsTesting {

    def "state for new user"() {
        given:
        def balances = freshBalances()

        when:
        def summary = balances.get()

        then:
        summary.header == zeroHeader()
        summary.credits == []
        summary.debits == []
        summary.cleared == []
    }

    def "applys operstion"() {
        given:
        def balances = freshBalances()
        def operation = loan(exampleUser(), exampleCounterParty(), money(25), money(25))

        when:
        balances.apply(operation)
        def summary = balances.get()

        then:
        summary.header == header(money(25), Money.zero())
        summary.credits == [entry(
                exampleCounterParty(),
                money(25),
                operation.details.timestamp
        )]
        summary.debits == []
        summary.cleared == []
    }

    def "overrides previous balance"() {
        given:
        def balances = freshBalances()
        balances.apply(loan(exampleUser(), exampleCounterParty(), money(25), money(25)))
        def operation = payback(exampleCounterParty(), exampleUser(), money(25), money(0))

        when:
        balances.apply(operation)
        def summary = balances.get()

        then:
        summary.header == header(Money.zero(), Money.zero())
        summary.credits == []
        summary.debits == []
        summary.cleared == [entry(
                exampleCounterParty(),
                money(0),
                operation.details.timestamp
        )]
    }

    def "sums balances"() {
        given:
        def balances = freshBalances()
        balances.apply(loan(exampleUser(), exampleCounterParty(), money(25), money(25)))
        balances.apply(loan(exampleUser(), anotherUser4(), money(5), money(25)))
        balances.apply(loan(anotherUser5(), exampleUser(), money(10), money(10)))
        balances.apply(loan(anotherUser6(), exampleUser(), money(15), money(15)))

        when:
        def summary = balances.get()

        then:
        summary.header == header(money(50), money(-25))
    }

    def "groups multiple"() {
        given:
        def balances = freshBalances()
        def credits = [
                loan(exampleUser(), anotherUser3(), money(25), money(25)),
                loan(exampleUser(), anotherUser4(), money(10), money(10)),
        ]
        def debits = [
                payback(anotherUser5(), exampleUser(), money(25), money(10)),
                loan(anotherUser6(), exampleUser(), money(10), money(15)),
        ]
        def cleared = [
                payback(anotherUser7(), exampleUser(), money(15), money(0)),
                payback(exampleUser(), anotherUser8(), money(15), money(0)),
        ]

        when:
        (credits + debits + cleared).forEach({ op -> balances.apply(op) })
        def summary = balances.get()

        then:
        summary.header == header(money(35), money(-25))
        summary.credits.collect { new Tuple(it.counterParty.id, it.balance) } == [
                new Tuple(anotherUser3(), money(25)),
                new Tuple(anotherUser4(), money(10)),
        ]
        summary.debits.collect { new Tuple(it.counterParty.id, it.balance) } == [
                new Tuple(anotherUser6(), money(-15)),
                new Tuple(anotherUser5(), money(-10)),
        ]
        summary.cleared.collect { new Tuple(it.counterParty.id, it.balance) } == [
                new Tuple(anotherUser7(), money(0)),
                new Tuple(anotherUser8(), money(0)),
        ]
    }

    def entry(UserId userId, Money balance, Instant timestamp) {
        new BalancesSummary.Entry(userDetails().get(userId), balance, timestamp)
    }

    BalancesState freshBalances() {
        new BalancesState(new BalancesId(0, 0), exampleUser(), new HashMap<UserId, BalancesSummary.Entry>(), userDetails())
    }

    BalancesSummary.Header zeroHeader() {
        new BalancesSummary.Header(Money.zero(), Money.zero(), Money.zero())
    }

    BalancesSummary.Header header(Money credits = Money.zero(), Money debits = Money.zero()) {
        new BalancesSummary.Header(credits, debits, credits.add(debits))
    }
}
