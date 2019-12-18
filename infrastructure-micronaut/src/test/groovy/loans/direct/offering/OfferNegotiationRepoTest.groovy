package loans.direct.offering

import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import spock.lang.Specification

import javax.inject.Inject
import java.time.Clock

@MicronautTest
class OfferNegotiationRepoTest extends Specification implements OfferTesting {

    @Inject
    OfferNegotiationAsJsonInDocumentStoreRepo repo

    @MockBean(Clock.FixedClock)
    Clock clock() {
        this.clock
    }

    def "changes are persistent"() {
        given:
        def offer = newOffer(exampleCreditor())
        repo.save(offer)

        when:
        offer = repo.get(offer.id)
        def transaction = offer.accept(exampleDebiter())
        repo.save(offer)

        then:
        transaction.details.from == exampleCreditor()
        transaction.details.to == exampleDebiter()
        transaction.details.amount == money(100)

    }
}
