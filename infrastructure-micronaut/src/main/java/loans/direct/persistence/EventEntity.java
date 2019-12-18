package loans.direct.persistence;

import lombok.Data;

@Data
public class EventEntity {
    long id;
    long object_id;
    long version;
    String type;
    String payload;
}
