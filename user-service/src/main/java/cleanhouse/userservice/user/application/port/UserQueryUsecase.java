package cleanhouse.userservice.user.application.port;

import cleanhouse.userservice.user.application.dto.UserListResponse;
import cleanhouse.userservice.user.application.dto.UserResponse;
import cleanhouse.userservice.user.application.query.GetCurrentUserQuery;
import cleanhouse.userservice.user.application.query.GetUsersQuery;

public interface UserQueryUsecase {
    UserListResponse getUsers(GetUsersQuery query);
    UserResponse getCurrentUser(GetCurrentUserQuery query);
}
