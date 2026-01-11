package cleanhouse.userservice.user.application.dto;

import cleanhouse.userservice.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String userId;
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
