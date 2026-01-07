package cleanhouse.userservice.security.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthErrorResponse {
    private String error;
    private String message;
    private String timestamp;
}
