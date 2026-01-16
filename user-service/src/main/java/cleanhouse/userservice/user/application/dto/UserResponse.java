package cleanhouse.userservice.user.application.dto;

import cleanhouse.userservice.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String userId;
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdAt;
    private List<OrderResponse> orders;

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .userId(user.getUserId())
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .build();
    }

    public void setOrders(List<OrderResponse> orders) {
        this.orders = orders;
    }
}
