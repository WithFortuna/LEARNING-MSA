package cleanhouse.userservice.user.domain.port;

import cleanhouse.userservice.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
