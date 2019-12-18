package loans.direct.offering;

import loans.direct.Event;
import loans.direct.Money;
import loans.direct.UserId;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@AllArgsConstructor
class OfferNegotiationFactory {

    private Supplier<OfferId> ids;
    private Supplier<UserId> loggedUser;
    private List<Event> events;
    private Clock clock;

    OfferNegotiation propose(UserId debtor, Money money) {
        OfferId id = ids.get();
        UserId user = loggedUser.get();
        OfferCreated initiated = OfferCreated.byCreditorSide(id, Instant.now(clock))
                .details(user, debtor, money);
        events.add(initiated);
        return new OfferNegotiation(id, initiated, null, null, events, clock);
    }

    OfferNegotiation ask(UserId creditor, Money money) {
        OfferId id = ids.get();
        UserId user = loggedUser.get();
        OfferCreated initiated = OfferCreated.byDebitorSide(id, Instant.now(clock))
                .details(creditor, user, money);
        events.add(initiated);
        return new OfferNegotiation(id, initiated, null, null, events, clock);
    }
}
