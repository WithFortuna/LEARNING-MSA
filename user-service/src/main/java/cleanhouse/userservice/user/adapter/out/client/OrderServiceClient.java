package cleanhouse.userservice.user.adapter.out.client;

import cleanhouse.userservice.user.domain.application.dto.OrderListResponse;

public interface OrderServiceClient {
	OrderListResponse getOrders(String userId);
}
