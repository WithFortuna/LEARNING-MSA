package cleanhouse.orderservice.order.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    private String userId;
    private String productId;
    private Integer quantity;
    private BigDecimal price;
}
