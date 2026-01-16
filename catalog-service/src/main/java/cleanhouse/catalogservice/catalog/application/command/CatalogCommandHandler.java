package cleanhouse.catalogservice.catalog.application.command;

import cleanhouse.catalogservice.catalog.application.dto.CatalogResponse;
import cleanhouse.catalogservice.catalog.application.port.CatalogCommandUsecase;
import cleanhouse.catalogservice.catalog.domain.entity.Catalog;
import cleanhouse.catalogservice.catalog.domain.port.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogCommandHandler implements CatalogCommandUsecase {
    private final CatalogRepository catalogRepository;

    @Override
    @Transactional
    public CatalogResponse createCatalog(CreateCatalogCommand command) {
        String productId = UUID.randomUUID().toString();
        Catalog catalog = Catalog.create(
                productId,
                command.getProductName(),
                command.getStock(),
                command.getUnitPrice()
        );
        Catalog savedCatalog = catalogRepository.save(catalog);
        return CatalogResponse.from(savedCatalog);
    }
}
