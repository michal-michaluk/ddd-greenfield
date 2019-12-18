package loans.direct.users;

import loans.direct.UserId;
import lombok.Value;

@Value
public class UserDetail {
    UserId id;
    String name;
    String photo;
}
