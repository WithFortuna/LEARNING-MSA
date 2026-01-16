package cleanhouse.orderservice.order.domain.application.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetOrdersByUserIdQuery {
    private final String userId;
}
