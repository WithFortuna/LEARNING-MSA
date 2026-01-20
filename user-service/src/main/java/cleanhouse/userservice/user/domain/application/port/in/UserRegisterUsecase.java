package cleanhouse.userservice.user.domain.application.port.in;

import cleanhouse.userservice.user.domain.application.dto.UserRegisterCommand;
import cleanhouse.userservice.user.domain.exception.DuplicateEmailException;

public interface UserRegisterUsecase {
    Long register(UserRegisterCommand command) throws DuplicateEmailException;
}
