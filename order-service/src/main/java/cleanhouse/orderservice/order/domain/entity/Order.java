package cleanhouse.orderservice.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String userId;
    private String productId;
    private BigDecimal totalPrice;
    private Integer quantity;
    private LocalDateTime createdAt;

    public static Order create(
            String userId,
            String productId,
            BigDecimal totalPrice,
            Integer quantity
    ) {
        return new Order(
                null,
                userId,
                productId,
                totalPrice,
                quantity,
                LocalDateTime.now()
        );
    }
}
