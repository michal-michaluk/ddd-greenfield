package loans.direct.offering;

import loans.direct.Event;
import loans.direct.Money;
import loans.direct.TransactionDetails;
import loans.direct.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.function.Function;

@Value
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferCreated implements Event {
    OfferId id;
    InitiatingSide side;
    Instant timestamp;
    UserId creditor;
    UserId debtor;
    Money amount;

    public static OfferCreatedBuilder byDebitorSide(OfferId id, Instant timestamp) {
        return builder().id(id).side(InitiatingSide.DEBATER).timestamp(timestamp);
    }

    public static OfferCreatedBuilder byCreditorSide(OfferId id, Instant timestamp) {
        return builder().id(id).side(InitiatingSide.CREDITOR).timestamp(timestamp);
    }

    public boolean isCounterParty(UserId user) {
        return side.counterParty.apply(this).equals(user);
    }

    public TransactionDetails getDetails(Instant timestamp) {
        return new TransactionDetails(
                timestamp,
                creditor,
                debtor,
                amount
        );
    }

    public static class OfferCreatedBuilder {
        public OfferCreated details(UserId creditor, UserId debtor, Money amount) {
            return creditor(creditor)
                    .debtor(debtor)
                    .amount(amount)
                    .build();
        }
    }

    private enum InitiatingSide {
        DEBATER(OfferCreated::getDebtor, OfferCreated::getCreditor),
        CREDITOR(OfferCreated::getCreditor, OfferCreated::getDebtor);

        final Function<OfferCreated, UserId> initiator;
        final Function<OfferCreated, UserId> counterParty;

        InitiatingSide(Function<OfferCreated, UserId> initiator, Function<OfferCreated, UserId> counterParty) {
            this.initiator = initiator;
            this.counterParty = counterParty;
        }
    }
}
