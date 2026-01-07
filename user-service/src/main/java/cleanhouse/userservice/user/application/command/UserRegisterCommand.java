package cleanhouse.userservice.user.application.command;

import cleanhouse.userservice.user.application.dto.UserRegisterRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRegisterCommand {
    private final String email;
    private final String password;
    private final String name;

    public static UserRegisterCommand from(UserRegisterRequest request) {
        return new UserRegisterCommand(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );
    }
}
