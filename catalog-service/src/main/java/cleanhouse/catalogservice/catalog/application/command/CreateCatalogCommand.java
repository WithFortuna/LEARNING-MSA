package cleanhouse.catalogservice.catalog.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCatalogCommand {
    private String productName;
    private Integer stock;
    private BigDecimal unitPrice;
}
