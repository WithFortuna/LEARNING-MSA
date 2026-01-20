package cleanhouse.userservice.user.domain.application.dto;

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
