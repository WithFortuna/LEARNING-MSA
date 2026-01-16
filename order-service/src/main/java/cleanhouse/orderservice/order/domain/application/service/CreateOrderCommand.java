package cleanhouse.orderservice.order.domain.application.service;

import cleanhouse.orderservice.order.domain.application.dto.OrderCreateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOrderCommand {
    private final String userId;
    private final String productId;
    private final Integer quantity;
    private final BigDecimal price;

    public static CreateOrderCommand from(OrderCreateRequest request) {
        return new CreateOrderCommand(
            request.getUserId(),
            request.getProductId(),
            request.getQuantity(),
            request.getPrice()
        );
    }
}
