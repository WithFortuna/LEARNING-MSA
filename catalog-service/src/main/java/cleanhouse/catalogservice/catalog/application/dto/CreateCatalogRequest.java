package cleanhouse.catalogservice.catalog.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCatalogRequest {
    private String productName;
    private Integer stock;
    private BigDecimal unitPrice;
}
