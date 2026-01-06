package cleanhouse.secondservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/second-service")
@RestController
public class SecondServiceController {
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to Second Service";
	}

	@GetMapping("/message")
	public String message(@RequestHeader("filter-add-request") String header) {
		log.info("Header: {}", header);
		return "Hello from First Service";
	}
}
