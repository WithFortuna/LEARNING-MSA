package cleanhouse.catalogservice.catalog.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Catalog {
    private Long id;
    private String productId;
    private String productName;
    private Integer stock;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;

    public static Catalog create(
            String productId,
            String productName,
            Integer stock,
            BigDecimal unitPrice
    ) {
        return new Catalog(
                null,
                productId,
                productName,
                stock,
                unitPrice,
                null
        );
    }
}
