package loans.direct;

import lombok.Value;

@Value
public class UserId {

    private static final UserId SYSTEM_USER = new UserId(Integer.MAX_VALUE);

    private final long id;

    public static UserId system() {
        return SYSTEM_USER;
    }
}
