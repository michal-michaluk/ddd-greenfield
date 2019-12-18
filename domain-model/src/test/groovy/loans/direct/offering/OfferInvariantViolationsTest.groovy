package loans.direct.offering

import spock.lang.Specification

class OfferInvariantViolationsTest extends Specification implements OfferTesting {

    def "accept with wrong user"() {
        given:
        def factory = offerFactory(exampleDebiter())
        def offer = factory.ask(exampleCreditor(), money(15))

        when:
        offer.accept(exampleDebiter())

        then:
        thrown(IllegalArgumentException)
    }

    def "accept rejected"() {
        given:
        def factory = offerFactory(exampleDebiter())
        def offer = factory.ask(exampleCreditor(), money(15))

        when:
        def reject = offer.reject(exampleCreditor())
        offer.accept(exampleCreditor())

        then:
        reject != null
        thrown(IllegalStateException)
    }

    def "accept twice"() {
        given:
        def factory = offerFactory(exampleDebiter())
        def offer = factory.ask(exampleCreditor(), money(15))

        when:
        def accept = offer.accept(exampleCreditor())
        offer.accept(exampleCreditor())

        then:
        accept != null
        thrown(IllegalStateException)
    }

    def "reject accepted"() {
        given:
        def factory = offerFactory(exampleDebiter())
        def offer = factory.ask(exampleCreditor(), money(15))

        when:
        def accept = offer.accept(exampleCreditor())
        offer.reject(exampleCreditor())

        then:
        accept != null
        thrown(IllegalStateException)
    }

    def "reject twice"() {
        given:
        def factory = offerFactory(exampleDebiter())
        def offer = factory.ask(exampleCreditor(), money(15))

        when:
        def reject = offer.reject(exampleCreditor())
        offer.reject(exampleCreditor())

        then:
        reject != null
        thrown(IllegalStateException)
    }
}
