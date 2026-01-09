package cleanhouse.catalogservice.catalog.domain.port;

import cleanhouse.catalogservice.catalog.domain.entity.Catalog;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository {
    Catalog save(Catalog catalog);
    Optional<Catalog> findById(Long id);
    Optional<Catalog> findByProductId(String productId);
    List<Catalog> findAll();
}
