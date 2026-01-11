package cleanhouse.userservice.user.application.query;

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
