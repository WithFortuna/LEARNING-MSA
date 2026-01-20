package cleanhouse.userservice.user.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUsersQuery {
    private int page;
    private int size;

    public static GetUsersQuery from(int page, int size) {
        return new GetUsersQuery(page, size);
    }
}
