package loans.direct.offerting;

import loans.direct.Event;
import loans.direct.TransactionDetails;
import lombok.Value;

@Value
public class TransactionRecorded implements Event {
    OfferId id;
    TransactionDetails details;
}
