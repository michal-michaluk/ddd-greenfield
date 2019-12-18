package loans.direct.offering

import loans.direct.Event
import loans.direct.Money
import loans.direct.TransactionDetails
import loans.direct.UserId

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

trait OfferTesting {

    Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    OfferNegotiationFactory offerFactory(UserId initiating) {
        new OfferNegotiationFactory({ someOfferId() }, { initiating }, noopEvents(), clock)
    }

    OfferNegotiation newOffer(UserId initiating) {
        new OfferNegotiation(someOfferId(), null, null, null, noopEvents(), clock)
    }

    OfferId someOfferId() {
        new OfferId(1, 0)
    }

    UserId exampleCreditor() {
        new UserId(1)
    }

    UserId exampleDebiter() {
        new UserId(2)
    }

    def details(UserId creditor, UserId debtor, Money amount) {
        new TransactionDetails(Instant.now(clock), creditor, debtor, amount)
    }

    Money money(def amount) {
        new Money(amount)
    }

    def noopEvents() {
        new ArrayList<Event>()
    }
}
