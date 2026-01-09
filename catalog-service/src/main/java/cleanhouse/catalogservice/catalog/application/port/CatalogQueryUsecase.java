package cleanhouse.catalogservice.catalog.application.port;

import cleanhouse.catalogservice.catalog.application.dto.CatalogListResponse;
import cleanhouse.catalogservice.catalog.application.query.GetCatalogsQuery;

public interface CatalogQueryUsecase {
    CatalogListResponse getCatalogs(GetCatalogsQuery query);
}
