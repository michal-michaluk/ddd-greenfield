package loans.direct.offerting;

import loans.direct.Event;
import loans.direct.Money;
import loans.direct.TransactionDetails;
import loans.direct.UserId;
import lombok.Value;

import java.time.Instant;

@Value
public class OfferCreated implements Event {
    OfferId id;
    UserId creditor;
    UserId debtor;
    Money amount;
    UserId counterParty;

    TransactionDetails getDetails(Instant timestamp) {
        return new TransactionDetails(timestamp, creditor, debtor, amount);
    }
}
