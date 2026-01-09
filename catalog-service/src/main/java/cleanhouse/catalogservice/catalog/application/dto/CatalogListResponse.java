package cleanhouse.catalogservice.catalog.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CatalogListResponse {
    private List<CatalogResponse> catalogs;
    private int totalCount;
}
