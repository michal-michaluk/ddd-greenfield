package loans.direct.offering;

import loans.direct.Event;
import loans.direct.UserId;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
class OfferNegotiation {
    OfferId id;
    private OfferCreated initiated;
    private TransactionRecorded transaction;
    private Rejected reject;

    List<Event> events;
    Clock clock;

    TransactionRecorded accept(UserId accepting) {
        offerActive();
        respondedByCounterParty(accepting);
        transaction = new TransactionRecorded(
                id,
                initiated.getDetails(Instant.now(clock))
        );
        events.add(transaction);
        return transaction;
    }

    Rejected reject(UserId rejecting) {
        offerActive();
        respondedByCounterParty(rejecting);
        reject = new Rejected(id, rejecting, Instant.now(clock));
        events.add(reject);
        return reject;
    }

    Rejected expire() {
        offerActive();
        reject = new Rejected(id, UserId.system(), Instant.now(clock));
        events.add(reject);
        return reject;
    }

    private void offerActive() {
        if (initiated == null) throw new IllegalStateException();
        if (transaction != null) throw new IllegalStateException();
        if (reject != null) throw new IllegalStateException();
    }

    private void respondedByCounterParty(UserId accepting) {
        if (!initiated.isCounterParty(accepting)) {
            throw new IllegalArgumentException();
        }
    }
}
