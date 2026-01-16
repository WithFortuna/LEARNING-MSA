package cleanhouse.orderservice.order.domain.application.dto;

import cleanhouse.orderservice.order.domain.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String userId;
    private String productId;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createdAt;

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getProductId(),
                order.getTotalPrice(),
                order.getQuantity(),
                order.getCreatedAt()
        );
    }
}
