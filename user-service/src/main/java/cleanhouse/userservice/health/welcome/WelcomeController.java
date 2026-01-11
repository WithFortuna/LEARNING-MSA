package cleanhouse.userservice.health.welcome;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WelcomeController {
	private final Environment environment;

	@GetMapping("/welcome")
	public String welcome(HttpServletRequest request) {
		return String.format("Welcome to the User Service.\nuser.welcome ip-> remote addr:%s, remote host:%s, request URI:%s, request URL:%s, greeting ment: %s\nfor you: %s and default profile: %s",
			request.getRemoteAddr(),
			request.getRemoteHost(),
			request.getRequestURI(),
			request.getRequestURL(),
			environment.getProperty("freshmen.ment"),
			environment.getProperty("for.you"),
			environment.getProperty("for.default-profile")
		);
	}
}
