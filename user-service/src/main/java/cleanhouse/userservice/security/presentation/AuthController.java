package cleanhouse.userservice.security.presentation;

import cleanhouse.userservice.security.application.command.RefreshTokenCommand;
import cleanhouse.userservice.security.application.dto.RefreshTokenRequest;
import cleanhouse.userservice.security.application.dto.RefreshTokenResponse;
import cleanhouse.userservice.security.application.port.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh attempt");
        RefreshTokenCommand command = RefreshTokenCommand.from(request);
        RefreshTokenResponse response = tokenService.refreshToken(command);
        return ResponseEntity.ok(response);
    }
}
