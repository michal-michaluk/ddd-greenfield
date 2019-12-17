package loans.direct.offerting

import spock.lang.Specification

class OfferInvariantViolationsTest extends Specification implements OfferTesting {

    def "accept with wrong user"() {
        given:
        def offer = newOffer(exampleDebtor())
        offer.ask(exampleCreditor(), money(15))

        when:
        offer.accept(exampleDebtor())

        then:
        thrown(IllegalArgumentException)
    }

    def "accept rejected"() {
        given:
        def offer = newOffer(exampleDebtor())
        offer.ask(exampleCreditor(), money(15))

        when:
        def reject = offer.reject(exampleCreditor())
        offer.accept(exampleCreditor())

        then:
        reject != null
        thrown(IllegalStateException)
    }

    def "accept twice"() {
        given:
        def offer = newOffer(exampleDebtor())
        offer.ask(exampleCreditor(), money(15))

        when:
        def accept = offer.accept(exampleCreditor())
        offer.accept(exampleCreditor())

        then:
        accept != null
        thrown(IllegalStateException)
    }

    def "reject accepted"() {
        given:
        def offer = newOffer(exampleDebtor())
        offer.ask(exampleCreditor(), money(15))

        when:
        def accept = offer.accept(exampleCreditor())
        offer.reject(exampleCreditor())

        then:
        accept != null
        thrown(IllegalStateException)
    }

    def "reject twice"() {
        given:
        def offer = newOffer(exampleDebtor())
        offer.ask(exampleCreditor(), money(15))

        when:
        def reject = offer.reject(exampleCreditor())
        offer.reject(exampleCreditor())

        then:
        reject != null
        thrown(IllegalStateException)
    }
}
