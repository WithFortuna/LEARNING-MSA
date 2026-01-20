package cleanhouse.userservice.common.exception;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String s, Response response) {
		switch (response.status()) {
			case 400 -> {
				break;
			}
			case 404 -> {
				if (s.contains("OrderServiceFeignClient#getOrders")) {
					return new ResponseStatusException(HttpStatus.valueOf(response.status()), "No orders found");
				}
			}
			default -> {
				return new Exception(response.reason());
			}
		}
		return null;
	}
}
