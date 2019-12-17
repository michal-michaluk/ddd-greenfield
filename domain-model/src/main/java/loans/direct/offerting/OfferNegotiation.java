package loans.direct.offerting;

import loans.direct.Money;
import loans.direct.UserId;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.Instant;

@AllArgsConstructor
class OfferNegotiation {

    private final OfferId id;
    private final UserId userId;
    private final Clock clock;
    private OfferCreated initiated;
    private boolean closed;

    OfferCreated propose(UserId debtor, Money money) {
        isNotInitiated();
        initiated = new OfferCreated(
                id, userId, debtor, money, debtor
        );
        return initiated;
    }

    OfferCreated ask(UserId creditor, Money money) {
        isNotInitiated();
        initiated = new OfferCreated(
                id, creditor, userId, money, creditor
        );
        return initiated;
    }

    TransactionRecorded accept(UserId accepting) {
        isActive();
        isCounterParty(accepting);
        closed = true;
        return new TransactionRecorded(id, initiated.getDetails(Instant.now(clock)));
    }


    Rejected reject(UserId rejecting) {
        isActive();
        isCounterParty(rejecting);
        closed = true;
        return new Rejected(id, rejecting);
    }

    private void isNotInitiated() {
        if (initiated != null) {
            throw new IllegalStateException();
        }
    }

    private void isActive() {
        if (initiated == null || closed) {
            throw new IllegalStateException();
        }
    }

    private void isCounterParty(UserId user) {
        if (!user.equals(initiated.getCounterParty())) {
            throw new IllegalArgumentException();
        }
    }
}
