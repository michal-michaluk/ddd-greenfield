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

    OfferNegotiation newOffer(UserId initiating) {
        def initiated = OfferCreated.byCreditorSide(someOfferId(), Instant.now(clock)).details(exampleCreditor(), exampleDebiter(), Money.of(100))
        new OfferNegotiation(someOfferId(), initiated, null, null, new ArrayList<Event>(), clock)
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
}
