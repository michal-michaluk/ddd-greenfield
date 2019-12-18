package loans.direct.offering;

import loans.direct.Event;
import loans.direct.UserId;
import lombok.Value;

import java.time.Instant;

@Value
public class Rejected implements Event {
    OfferId id;
    UserId rejecting;
    Instant timestamp;
}
