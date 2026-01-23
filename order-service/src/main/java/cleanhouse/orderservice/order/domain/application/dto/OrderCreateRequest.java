package cleanhouse.orderservice.order.domain.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    @NotBlank
    private String userId;
    @NotBlank
    private String productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private BigDecimal price;
}
