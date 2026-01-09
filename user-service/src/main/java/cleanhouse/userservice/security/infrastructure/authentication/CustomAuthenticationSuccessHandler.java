package cleanhouse.userservice.security.infrastructure.authentication;

import cleanhouse.userservice.security.infrastructure.jwt.JwtTokenProvider;
import cleanhouse.userservice.security.infrastructure.token.RefreshTokenStoreAdapter;
import cleanhouse.userservice.security.infrastructure.userdetails.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenStoreAdapter refreshTokenStoreAdapter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        String role = userDetails.getRole();

        String accessToken = jwtTokenProvider.generateAccessToken(email, role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        long refreshTokenExpiration = jwtTokenProvider.getExpirationTimeMs(refreshToken);
        refreshTokenStoreAdapter.save(email, refreshToken, refreshTokenExpiration);

        log.info("User logged in successfully: {}", email);

        response.setHeader("accessToken", accessToken);
        response.addCookie(new Cookie("refreshToken", refreshToken));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
