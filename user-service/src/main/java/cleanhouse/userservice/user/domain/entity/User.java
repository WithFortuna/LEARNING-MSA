package cleanhouse.userservice.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String userId;
    private String email;
    private String password;
    private String name;
    private String role;
    private LocalDateTime createdAt;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userId = UUID.randomUUID().toString();
        this.role = Role.ROLE_USER.getValue();
        this.createdAt = LocalDateTime.now();
    }

    public User setEncryptedPassword(String encryptedPassword) {
        return new User(this.id, this.userId, this.email, encryptedPassword, this.name, this.role, this.createdAt);
    }

    public User withRole(String role) {
        return new User(this.id, this.userId, this.email, this.password, this.name, role, this.createdAt);
    }

}
