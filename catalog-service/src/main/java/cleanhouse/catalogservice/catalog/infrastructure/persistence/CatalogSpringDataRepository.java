package cleanhouse.catalogservice.catalog.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogSpringDataRepository extends JpaRepository<CatalogEntity, Long> {
    Optional<CatalogEntity> findByProductId(String productId);
}
