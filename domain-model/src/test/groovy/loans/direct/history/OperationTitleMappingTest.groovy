package loans.direct.history


import loans.direct.UserId
import spock.lang.Specification

class OperationTitleMappingTest extends Specification implements OperationsTesting {

    def "01.11.2019 Maciek pożyczył od Ciebie kwotę 25 PLN, saldo po operacji 25 PLN"() {
        given:
        def history = titlesFor(exampleUser(), exampleCounterPartyName())

        when:
        def title = history.map(loan(exampleUser(), exampleCounterParty(), money(25), balance(money(25))))

        then:
        title == "Maciek pożyczył od Ciebie kwotę 25 PLN, saldo po operacji 25 PLN"
    }

    def "02.11.2019 Maciek zwrócił Ci kwotę 20 PLN, saldo po operacji 5 PLN"() {
        given:
        def history = titlesFor(exampleUser(), exampleCounterPartyName())

        when:
        def title = history.map(payback(exampleCounterParty(), exampleUser(), money(20),
                balance(money(-5))))

        then:
        title == "Maciek zwrócił Ci kwotę 20 PLN, saldo po operacji 5 PLN"
    }

    def "03.11.2019 Maciek pożyczył Ci kwotę 15 PLN, saldo po operacji minus10 PLN"() {
        given:
        def history = titlesFor(exampleUser(), exampleCounterPartyName())

        when:
        def title = history.map(loan(exampleCounterParty(), exampleUser(), money(15),
                balance(money(10))))

        then:
        title == "Maciek pożyczył Ci kwotę 15 PLN, saldo po operacji -10 PLN"
    }

    def "04.11.2019 Maciek otrzymał od Ciebie kwotę 10 PLN, saldo po operacji 0 PLN"() {
        given:
        def history = titlesFor(exampleUser(), exampleCounterPartyName())

        when:
        def title = history.map(payback(exampleUser(), exampleCounterParty(), money(10),
                balance(money(0))))

        then:
        title == "Maciek otrzymał od Ciebie kwotę 10 PLN, saldo po operacji 0 PLN"
    }

    private OperationTitleMapping titlesFor(UserId user, String name) {
        new OperationTitleMapping(user, name)
    }
}
