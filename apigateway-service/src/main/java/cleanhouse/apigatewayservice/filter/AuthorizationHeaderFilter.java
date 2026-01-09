package cleanhouse.apigatewayservice.filter;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import cleanhouse.apigatewayservice.domain.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
	@Value("${jwt.secret-key}")
	private String secretKey;

	public AuthorizationHeaderFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			log.info("API gateway 내부 인증/인가 필터 시작 Authorization header filter start");

			ServerHttpRequest request = exchange.getRequest();

			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No authorization header: " + request.getURI());
			}

			String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String accessToken = authorizationHeader.replace("Bearer ", "");

			if (!validateToken(accessToken)) {
				return onError(exchange, "token not validate: " + request.getURI());
			}

			log.info("API gateway 내부 인증/인가 필터 종료 Authorization header filter end");

			return chain.filter(exchange);
		});
	}

	private Mono<Void> onError(ServerWebExchange exchange, String errorLog) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		log.error(errorLog);

		return response.setComplete();
	}

	public boolean validateToken(String token) {
		try {
			extractClaims(token);
			return true;
		} catch (InvalidTokenException e) {
			log.error("Token validation failed: {}", e.getMessage());
			return false;
		}
	}

	private Claims extractClaims(String token) {
		try {
			return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (ExpiredJwtException e) {
			throw new InvalidTokenException("Token has expired", e);
		} catch (UnsupportedJwtException e) {
			throw new InvalidTokenException("Unsupported JWT token", e);
		} catch (MalformedJwtException e) {
			throw new InvalidTokenException("Invalid JWT token", e);
		} catch (SecurityException e) {
			throw new InvalidTokenException("Invalid JWT signature", e);
		} catch (IllegalArgumentException e) {
			throw new InvalidTokenException("JWT claims string is empty", e);
		}
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)) ;
	}

	public static class Config {

	}
}
