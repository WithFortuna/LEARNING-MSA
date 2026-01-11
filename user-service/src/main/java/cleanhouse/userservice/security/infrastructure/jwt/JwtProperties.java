package cleanhouse.userservice.security.infrastructure.jwt;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private String secret;
	private Long accessTokenExpiration;
	private Long refreshTokenExpiration;
}
