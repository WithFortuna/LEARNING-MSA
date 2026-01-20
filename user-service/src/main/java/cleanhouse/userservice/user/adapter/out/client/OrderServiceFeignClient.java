package cleanhouse.userservice.user.adapter.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cleanhouse.userservice.common.exception.FeignErrorDecoder;
import cleanhouse.userservice.user.domain.application.dto.OrderListResponse;

@FeignClient(name = "order-service", configuration = FeignErrorDecoder.class)	// service-discovery에 등록된 호출할 서버의 application name & OpenFeign 라이브러리가 빈으로 등록도 해줌
public interface OrderServiceFeignClient extends OrderServiceClient{
	@Override
	@GetMapping("/{userId}/orders/wrongEndpoint")
	OrderListResponse getOrders(@PathVariable String userId);
}
