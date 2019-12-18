package loans.direct.history;

import lombok.Value;

import java.time.Instant;

@Value
public class OperationDescription {
    Instant timestamp;
    String title;
}
