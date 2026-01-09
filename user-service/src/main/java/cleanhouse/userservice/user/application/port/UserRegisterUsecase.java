package cleanhouse.userservice.user.application.port;

import cleanhouse.userservice.user.application.command.UserRegisterCommand;
import cleanhouse.userservice.user.domain.exception.DuplicateEmailException;

public interface UserRegisterUsecase {
    Long register(UserRegisterCommand command) throws DuplicateEmailException;
}
