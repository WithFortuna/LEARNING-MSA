package cleanhouse.userservice.user.application.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCurrentUserQuery {
    private String email;

    public static GetCurrentUserQuery from(String email) {
        return new GetCurrentUserQuery(email);
    }
}
