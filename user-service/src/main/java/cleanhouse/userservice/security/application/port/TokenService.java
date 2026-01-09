package cleanhouse.userservice.security.application.port;

import cleanhouse.userservice.security.application.command.RefreshTokenCommand;
import cleanhouse.userservice.security.application.dto.RefreshTokenResponse;

public interface TokenService {
    RefreshTokenResponse refreshToken(RefreshTokenCommand command);
}
