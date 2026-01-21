package cleanhouse.userservice.security.application.command;

import cleanhouse.userservice.security.application.dto.RefreshTokenResponse;
import cleanhouse.userservice.security.application.port.TokenService;
import cleanhouse.userservice.security.domain.exception.InvalidTokenException;
import cleanhouse.userservice.security.infrastructure.jwt.JwtTokenProvider;
import cleanhouse.userservice.security.infrastructure.token.RefreshTokenStoreAdapter;
import cleanhouse.userservice.user.domain.entity.User;
import cleanhouse.userservice.user.domain.exception.UserNotFoundException;
import cleanhouse.userservice.user.domain.application.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenCommandHandler implements TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenStoreAdapter refreshTokenStoreAdapter;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(RefreshTokenCommand command) {
        String refreshToken = command.getRefreshToken();

        // 1. Validate refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // 2. Check token type
        String tokenType = jwtTokenProvider.extractTokenType(refreshToken);
        if (!"REFRESH".equals(tokenType)) {
            throw new InvalidTokenException("Token is not a refresh token");
        }

        // 3. Extract email from token
        String email = jwtTokenProvider.extractEmail(refreshToken);

        // 4. Check if token exists in whitelist
        String storedToken = refreshTokenStoreAdapter.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found or has been revoked"));

        // 5. Verify the token matches the stored one
        if (!storedToken.equals(refreshToken)) {
            log.warn("Refresh token mismatch for user: {}", email);
            throw new InvalidTokenException("Refresh token does not match");
        }

        // 6. Load user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // 7. Generate new access token
        String newAccessToken = jwtTokenProvider.generateAccessToken(email, user.getRole());

        // 8. Generate new refresh token (token rotation)
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        // 9. Delete old refresh token from Redis
        refreshTokenStoreAdapter.delete(email);

        // 10. Store new refresh token in Redis
        long newRefreshTokenExpiration = jwtTokenProvider.getExpirationTimeMs(newRefreshToken);
        refreshTokenStoreAdapter.save(email, newRefreshToken, newRefreshTokenExpiration);

        log.info("Token refreshed successfully for user: {}", email);

        // 11. Return new tokens
        return new RefreshTokenResponse(newAccessToken, newRefreshToken, "Bearer");
    }

}
