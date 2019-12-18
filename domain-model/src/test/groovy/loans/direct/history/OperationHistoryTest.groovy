package loans.direct.history


import spock.lang.Specification

import java.time.Instant
import java.util.stream.Collectors

class OperationHistoryTest extends Specification implements OperationsTesting {

    def history = [
            loan(exampleUser(), exampleCounterParty(), money(25), balance(money(25))),
            payback(exampleCounterParty(), exampleUser(), money(20), balance(money(-5))),
            loan(exampleCounterParty(), exampleUser(), money(15), balance(money(10))),
            payback(exampleUser(), exampleCounterParty(), money(10), balance(money(0)))
    ]

    def query = new HistoryQuery(userDetails(), history)

    def "One user perspective on all operations"() {
        when:
        def descriptions = query.history(new AccountKey(exampleUser(), exampleCounterParty()), Instant.MIN, Instant.MAX)
                .map { desc -> desc.title }
                .collect(Collectors.toList())

        then:
        descriptions == [
                "Maciek pożyczył od Ciebie kwotę 25 PLN, saldo po operacji 25 PLN",
                "Maciek zwrócił Ci kwotę 20 PLN, saldo po operacji 5 PLN",
                "Maciek pożyczył Ci kwotę 15 PLN, saldo po operacji -10 PLN",
                "Maciek otrzymał od Ciebie kwotę 10 PLN, saldo po operacji 0 PLN"
        ]
    }

    def "Second user perspective on all operations"() {
        when:
        def descriptions = query.history(new AccountKey(exampleCounterParty(), exampleUser()), Instant.MIN, Instant.MAX)
                .map { desc -> desc.title }
                .collect(Collectors.toList())

        then:
        descriptions == [
                "Michał pożyczył Ci kwotę 25 PLN, saldo po operacji -25 PLN",
                "Michał otrzymał od Ciebie kwotę 20 PLN, saldo po operacji -5 PLN",
                "Michał pożyczył od Ciebie kwotę 15 PLN, saldo po operacji 10 PLN",
                "Michał zwrócił Ci kwotę 10 PLN, saldo po operacji 0 PLN"
        ]
    }

}
