package loans.direct.offering

import loans.direct.UserId
import spock.lang.Specification

class OfferScenariosTest extends Specification implements OfferTesting {

    def "propose then accept"() {
        given:
        def factory = offerFactory(exampleCreditor())

        when:
        def offer = factory.propose(exampleDebiter(), money(20))
        def initiated = offer.initiated

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebiter()
        initiated.isCounterParty(exampleDebiter())

        when:
        def transaction = offer.accept(exampleDebiter())

        then:
        transaction.details == details(exampleCreditor(), exampleDebiter(), money(20))
    }

    def "propose then reject"() {
        given:
        def factory = offerFactory(exampleCreditor())

        when:
        def offer = factory.propose(exampleDebiter(), money(20))
        def initiated = offer.initiated

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebiter()
        initiated.isCounterParty(exampleDebiter())

        when:
        def reject = offer.reject(exampleDebiter())

        then:
        reject.rejecting == exampleDebiter()
    }

    def "propose then expire"() {
        given:
        def factory = offerFactory(exampleCreditor())

        when:
        def offer = factory.propose(exampleDebiter(), money(20))
        def initiated = offer.initiated

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebiter()
        initiated.isCounterParty(exampleDebiter())

        when:
        def reject = offer.expire()

        then:
        reject.rejecting == UserId.system()
    }

    def "ask then accept"() {
        given:
        def factory = offerFactory(exampleDebiter())

        when:
        def offer = factory.ask(exampleCreditor(), money(20))
        def initiated = offer.initiated

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebiter()
        initiated.isCounterParty(exampleCreditor())

        when:
        def transaction = offer.accept(exampleCreditor())

        then:
        transaction.details == details(exampleCreditor(), exampleDebiter(), money(20))
    }

    def "ask then reject"() {
        given:
        def factory = offerFactory(exampleDebiter())

        when:
        def offer = factory.ask(exampleCreditor(), money(20))
        def initiated = offer.initiated

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebiter()
        initiated.isCounterParty(exampleCreditor())

        when:
        def reject = offer.reject(exampleCreditor())

        then:
        reject.rejecting == exampleCreditor()
    }

    def "ask then expire"() {
        given:
        def factory = offerFactory(exampleDebiter())

        when:
        def offer = factory.ask(exampleCreditor(), money(20))
        def initiated = offer.initiated

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebiter()
        initiated.isCounterParty(exampleCreditor())

        when:
        def reject = offer.expire()

        then:
        reject.rejecting == UserId.system()
    }
}
