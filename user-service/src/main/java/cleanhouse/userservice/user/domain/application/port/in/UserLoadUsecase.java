package cleanhouse.userservice.user.domain.application.port.in;

import cleanhouse.userservice.user.domain.application.dto.UserListResponse;
import cleanhouse.userservice.user.domain.application.dto.UserResponse;
import cleanhouse.userservice.user.domain.application.dto.GetCurrentUserQuery;
import cleanhouse.userservice.user.domain.application.dto.GetUsersQuery;

public interface UserLoadUsecase {
    UserListResponse getUsers(GetUsersQuery query);
    UserResponse getCurrentUser(GetCurrentUserQuery query);
}
