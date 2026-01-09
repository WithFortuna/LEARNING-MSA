package cleanhouse.userservice.security.infrastructure.jwt;

import cleanhouse.userservice.security.domain.exception.InvalidTokenException;
import cleanhouse.userservice.security.infrastructure.token.TokenBlacklistAdapter;
import cleanhouse.userservice.security.infrastructure.userdetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final TokenBlacklistAdapter tokenBlacklistAdapter;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/auth/login",
            "/auth/refresh",
            "/users",
            "/health",
            "/welcome"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (requestIncludeToken(jwt)) {
                if (tokenBlacklistAdapter.isBlacklisted(jwt)) {
                    log.warn("Attempted to use blacklisted token");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been revoked");
                    return;
                }

                if (jwtTokenProvider.validateToken(jwt)) {
                    String email = jwtTokenProvider.extractEmail(jwt);
                    String tokenType = jwtTokenProvider.extractTokenType(jwt);

                    if (!"ACCESS".equals(tokenType)) {
                        log.warn("Attempted to use non-access token for authentication");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token type");
                        return;
                    }

                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Set authentication for user: {}", email);
                }
            }
        } catch (InvalidTokenException e) {
            log.error("Could not set user authentication: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private static boolean requestIncludeToken(String jwt) {
        return StringUtils.hasText(jwt);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        return EXCLUDED_PATHS.stream()
                .anyMatch(excluded -> {
                    if (excluded.equals("/users") && "POST".equals(method)) {
                        return path.equals(excluded);
                    }
                    return path.startsWith(excluded);
                });
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (requestIncludeToken(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
