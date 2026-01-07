package cleanhouse.userservice.health.welcome;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class WelcomeController {
	@GetMapping("/welcome")
	public String welcome(HttpServletRequest request) {
		log.info("user.welcome ip-> remote addr:{}, remote host:{}, request URI:{}, request URL:{}",
			request.getRemoteAddr(),
			request.getRemoteHost(),
			request.getRequestURI(),
			request.getRequestURL()
		);
		return "Welcome to the User Service.";
	}
}
