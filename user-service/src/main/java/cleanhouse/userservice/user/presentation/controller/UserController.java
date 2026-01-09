package cleanhouse.userservice.user.presentation.controller;

import cleanhouse.userservice.user.application.command.UserRegisterCommand;
import cleanhouse.userservice.user.application.dto.UserRegisterRequest;
import cleanhouse.userservice.user.application.port.UserRegisterUsecase;
import cleanhouse.userservice.user.domain.exception.DuplicateEmailException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRegisterUsecase userRegisterUsecase;

    @PostMapping("/users")
    public ResponseEntity<Long> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        log.info("Registering user: {}", request.getEmail());
        try {
            // Map request to command using static factory method
            log.info("Registering user: {}, password:{}", request.getEmail(),request.getPassword());
			// Execute command
            Long userId = userRegisterUsecase.register(UserRegisterCommand.from(request));

            // Return user ID with HTTP 201
            return ResponseEntity.status(HttpStatus.CREATED).body(userId);
        } catch (DuplicateEmailException e) {
            // Return HTTP 409 for duplicate email
            log.warn("Duplicate email registration attempt: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            // Return HTTP 400 for validation errors
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
