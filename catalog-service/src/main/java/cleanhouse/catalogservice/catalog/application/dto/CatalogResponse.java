package cleanhouse.catalogservice.catalog.application.dto;

import cleanhouse.catalogservice.catalog.domain.entity.Catalog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CatalogResponse {
    private Long id;
    private String productId;
    private String productName;
    private Integer stock;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;

    public static CatalogResponse from(Catalog catalog) {
        return new CatalogResponse(
                catalog.getId(),
                catalog.getProductId(),
                catalog.getProductName(),
                catalog.getStock(),
                catalog.getUnitPrice(),
                catalog.getCreatedAt()
        );
    }
}
