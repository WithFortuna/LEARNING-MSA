package cleanhouse.userservice.security.application.command;

import cleanhouse.userservice.security.domain.exception.InvalidTokenException;
import cleanhouse.userservice.security.infrastructure.jwt.JwtTokenProvider;
import cleanhouse.userservice.security.infrastructure.token.RefreshTokenStoreAdapter;
import cleanhouse.userservice.security.infrastructure.token.TokenBlacklistAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutCommandHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistAdapter tokenBlacklistAdapter;
    private final RefreshTokenStoreAdapter refreshTokenStoreAdapter;

    @Transactional
    public void logout(LogoutCommand command) {
        String accessToken = command.getAccessToken();
        String email = command.getEmail();

        // 1. Validate access token
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new InvalidTokenException("Invalid or expired access token");
        }

        // 2. Extract expiration time and calculate TTL
        long ttl = jwtTokenProvider.getExpirationTimeMs(accessToken);

        if (ttl <= 0) {
            log.warn("Access token already expired for user: {}", email);
            // Even if expired, still remove refresh token
        } else {
            // 3. Add access token to blacklist with TTL
            tokenBlacklistAdapter.addToBlacklist(accessToken, ttl);
            log.info("Access token added to blacklist for user: {}", email);
        }

        // 4. Delete refresh token from whitelist
        refreshTokenStoreAdapter.delete(email);
        log.info("Refresh token removed from whitelist for user: {}", email);

        log.info("User logged out successfully: {}", email);
    }
}
