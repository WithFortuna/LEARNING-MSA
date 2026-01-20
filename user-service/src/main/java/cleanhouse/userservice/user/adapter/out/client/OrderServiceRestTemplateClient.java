package cleanhouse.userservice.user.adapter.out.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cleanhouse.userservice.user.domain.application.dto.OrderListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderServiceRestTemplateClient implements OrderServiceClient{
	@Value("${order-service.url}")
	private String orderServiceUrl;

	private final RestTemplate restTemplate;

	public OrderListResponse getOrders(String userId) {
		String requestUrl = String.format(orderServiceUrl + "/%s/orders", userId);

		log.info("Requesturl: {}", requestUrl);
		OrderListResponse orderListResponse = restTemplate.getForObject(requestUrl, OrderListResponse.class);

		log.info("Orders for user size: {}", orderListResponse.getOrders().size());

		return orderListResponse;
	}

}
