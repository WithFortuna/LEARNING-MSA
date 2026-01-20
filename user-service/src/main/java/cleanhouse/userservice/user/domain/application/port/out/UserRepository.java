package cleanhouse.userservice.user.domain.application.port.out;

import cleanhouse.userservice.user.domain.entity.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Page<User> findAll(int page, int size);
}
