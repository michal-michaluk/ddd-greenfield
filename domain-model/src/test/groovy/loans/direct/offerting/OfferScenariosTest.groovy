package loans.direct.offerting


import spock.lang.Specification

class OfferScenariosTest extends Specification implements OfferTesting {

    def "propose then accept"() {
        given:
        def offer = newOffer(exampleCreditor())

        when:
        def initiated = offer.propose(exampleDebtor(), money(20))

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebtor()
        initiated.getCounterParty() == exampleDebtor()

        when:
        def transaction = offer.accept(exampleDebtor())

        then:
        transaction.details == details(exampleCreditor(), exampleDebtor(), money(20))
    }

    def "propose then reject"() {
        given:
        def offer = newOffer(exampleCreditor())

        when:
        def initiated = offer.propose(exampleDebtor(), money(20))

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebtor()
        initiated.getCounterParty() == exampleDebtor()

        when:
        def reject = offer.reject(exampleDebtor())

        then:
        reject.rejecting == exampleDebtor()
    }


    def "ask then accept"() {
        given:
        def offer = newOffer(exampleDebtor())

        when:
        def initiated = offer.ask(exampleCreditor(), money(20))

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebtor()
        initiated.getCounterParty() == exampleCreditor()

        when:
        def transaction = offer.accept(exampleCreditor())

        then:
        transaction.details == details(exampleCreditor(), exampleDebtor(), money(20))
    }

    def "ask then reject"() {
        given:
        def offer = newOffer(exampleDebtor())

        when:
        def initiated = offer.ask(exampleCreditor(), money(20))

        then:
        initiated.creditor == exampleCreditor()
        initiated.debtor == exampleDebtor()
        initiated.getCounterParty() == exampleCreditor()

        when:
        def reject = offer.reject(exampleCreditor())

        then:
        reject.rejecting == exampleCreditor()
    }

}
