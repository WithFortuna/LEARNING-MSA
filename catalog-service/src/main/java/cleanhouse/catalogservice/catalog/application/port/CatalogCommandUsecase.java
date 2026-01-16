package cleanhouse.catalogservice.catalog.application.port;

import cleanhouse.catalogservice.catalog.application.command.CreateCatalogCommand;
import cleanhouse.catalogservice.catalog.application.dto.CatalogResponse;

public interface CatalogCommandUsecase {
    CatalogResponse createCatalog(CreateCatalogCommand command);
}
