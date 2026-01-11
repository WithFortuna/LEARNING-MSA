package cleanhouse.userservice.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserListResponse {
    private List<UserResponse> users;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
