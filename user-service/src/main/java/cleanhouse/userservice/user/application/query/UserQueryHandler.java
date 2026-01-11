package cleanhouse.userservice.user.application.query;

import cleanhouse.userservice.user.application.dto.UserListResponse;
import cleanhouse.userservice.user.application.dto.UserResponse;
import cleanhouse.userservice.user.application.port.UserQueryUsecase;
import cleanhouse.userservice.user.domain.entity.User;
import cleanhouse.userservice.user.domain.exception.UserNotFoundException;
import cleanhouse.userservice.user.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserQueryHandler implements UserQueryUsecase {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserListResponse getUsers(GetUsersQuery query) {
        Page<User> userPage = userRepository.findAll(query.getPage(), query.getSize());

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());

        return new UserListResponse(
                userResponses,
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.getNumber(),
                userPage.getSize()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(GetCurrentUserQuery query) {
        User user = userRepository.findByEmail(query.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + query.getEmail()));

        return UserResponse.from(user);
    }
}
