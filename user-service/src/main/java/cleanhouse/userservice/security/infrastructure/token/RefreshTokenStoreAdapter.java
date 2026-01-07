package cleanhouse.userservice.security.infrastructure.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenStoreAdapter {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_PREFIX = "refresh:";

    public void save(String email, String refreshToken, long expirationMs) {
        String key = REFRESH_PREFIX + email;
        redisTemplate.opsForValue().set(key, refreshToken, expirationMs, TimeUnit.MILLISECONDS);
    }

    public Optional<String> findByEmail(String email) {
        String key = REFRESH_PREFIX + email;
        String token = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(token);
    }

    public void delete(String email) {
        String key = REFRESH_PREFIX + email;
        redisTemplate.delete(key);
    }

    public boolean exists(String email) {
        String key = REFRESH_PREFIX + email;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
