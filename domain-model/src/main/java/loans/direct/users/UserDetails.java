package loans.direct.users;

import loans.direct.UserId;

public interface UserDetails {
    UserDetail get(UserId user);
}
