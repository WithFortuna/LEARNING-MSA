package cleanhouse.userservice.user.application.command;

import cleanhouse.userservice.user.application.port.UserRegisterUsecase;
import cleanhouse.userservice.user.domain.entity.User;
import cleanhouse.userservice.user.domain.exception.DuplicateEmailException;
import cleanhouse.userservice.user.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegisterCommandHandler implements UserRegisterUsecase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long register(UserRegisterCommand command) throws DuplicateEmailException {
        // Validate data
        if (command.getEmail() == null || command.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (command.getPassword() == null || command.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (command.getName() == null || command.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        // Check duplicate email
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new DuplicateEmailException(command.getEmail());
        }

        // Create domain entity
        User user = new User(
                command.getEmail(),
                command.getPassword(),
                command.getName()
        );

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        User userWithEncryptedPassword = user.setEncryptedPassword(encryptedPassword);

        // Save user
        User savedUser = userRepository.save(userWithEncryptedPassword);

        return savedUser.getId();
    }
}
