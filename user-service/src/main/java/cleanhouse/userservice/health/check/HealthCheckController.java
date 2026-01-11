package cleanhouse.userservice.health.check;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class HealthCheckController {
	private final Environment environment;

	@GetMapping("/health-check")
	public String healthCheck() {
		// server.port: .yml에 적은 포트(ex 0)
		// local.server.port: 실제 실행된 포트(차이가 있으려면 랜덤 포트일 때만 의미가 있음)
		return String.format("Service is up \nset server port: %s, and running on port %s\ntoken secret: %s",
			environment.getProperty("server.port"),
			environment.getProperty("local.server.port"),
			environment.getProperty("jwt.secret")
		);
		// return "hello this is health check";
	}
}
