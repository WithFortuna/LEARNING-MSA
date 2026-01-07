package cleanhouse.userservice.user.domain.exception;

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException(String email) {
        super("Email already registered: " + email);
    }
}
