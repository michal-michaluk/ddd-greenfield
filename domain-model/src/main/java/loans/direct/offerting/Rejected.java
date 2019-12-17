package loans.direct.offerting;

import loans.direct.Event;
import loans.direct.UserId;
import lombok.Value;

@Value
public class Rejected implements Event {
    OfferId id;
    UserId rejecting;
}
