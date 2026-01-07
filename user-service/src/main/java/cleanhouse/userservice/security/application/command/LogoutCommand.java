package cleanhouse.userservice.security.application.command;

import cleanhouse.userservice.security.application.dto.LogoutRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutCommand {
    private String accessToken;
    private String email;

    public static LogoutCommand from(LogoutRequest request, String email) {
        return new LogoutCommand(
                request.getAccessToken() != null ? request.getAccessToken() : "",
                email
        );
    }

    public static LogoutCommand from(String accessToken, String email) {
        return new LogoutCommand(accessToken, email);
    }
}
