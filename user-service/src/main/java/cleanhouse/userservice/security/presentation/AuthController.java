package cleanhouse.userservice.security.presentation;

import cleanhouse.userservice.security.application.command.RefreshTokenCommand;
import cleanhouse.userservice.security.application.dto.LoginRequest;
import cleanhouse.userservice.security.application.dto.RefreshTokenRequest;
import cleanhouse.userservice.security.application.dto.RefreshTokenResponse;
import cleanhouse.userservice.security.application.port.TokenService;
import cleanhouse.userservice.security.infrastructure.authentication.CustomAuthenticationFailureHandler;
import cleanhouse.userservice.security.infrastructure.authentication.CustomAuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final TokenService tokenService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginRequest request,
                      HttpServletRequest httpRequest,
                      HttpServletResponse httpResponse) throws Exception {
        log.info("Login attempt for user: {}", request.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            successHandler.onAuthenticationSuccess(httpRequest, httpResponse, authentication);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getEmail());
            failureHandler.onAuthenticationFailure(httpRequest, httpResponse, e);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh attempt");
        RefreshTokenCommand command = RefreshTokenCommand.from(request);
        RefreshTokenResponse response = tokenService.refreshToken(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<java.util.Map<String, String>> logout(HttpServletRequest request) {
        log.info("Logout attempt");

        // Extract access token from Authorization header
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Missing or invalid Authorization header"));
        }

        String accessToken = bearerToken.substring(7);

        // Get email from SecurityContext
        org.springframework.security.core.Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(java.util.Map.of("error", "User not authenticated"));
        }

        String email = authentication.getName();

        // Create logout command and execute
        cleanhouse.userservice.security.application.command.LogoutCommand command =
                cleanhouse.userservice.security.application.command.LogoutCommand.from(accessToken, email);
        tokenService.logout(command);

        log.info("User logged out successfully: {}", email);
        return ResponseEntity.ok(java.util.Map.of("message", "Logged out successfully"));
    }
}
