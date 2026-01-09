package cleanhouse.userservice.security.infrastructure.authentication;

import cleanhouse.userservice.security.application.command.LogoutCommand;
import cleanhouse.userservice.security.application.command.LogoutCommandHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final LogoutCommandHandler logoutCommandHandler;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("No authenticated user found for logout");
            return;
        }

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for logout");
            return;
        }

        String accessToken = bearerToken.substring(7);
        String email = authentication.getName();

        LogoutCommand command = LogoutCommand.from(accessToken, email);
        logoutCommandHandler.logout(command);

        log.info("Logout handler completed for user: {}", email);
    }
}
