package cleanhouse.orderservice.order.domain.application.port.in;

import cleanhouse.orderservice.order.domain.application.dto.OrderListResponse;
import cleanhouse.orderservice.order.domain.application.service.GetOrdersQuery;
import cleanhouse.orderservice.order.domain.application.service.GetOrdersByUserIdQuery;

public interface OrderQueryUsecase {
    OrderListResponse getOrders(GetOrdersQuery query);
    OrderListResponse getOrdersByUserId(GetOrdersByUserIdQuery query);
}
