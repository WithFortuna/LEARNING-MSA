package cleanhouse.userservice.security.application.command;

import cleanhouse.userservice.security.application.dto.RefreshTokenRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenCommand {
    private String refreshToken;

    public static RefreshTokenCommand from(RefreshTokenRequest request) {
        return new RefreshTokenCommand(request.getRefreshToken());
    }
}
