package cleanhouse.catalogservice.catalog.infrastructure.persistence;

import cleanhouse.catalogservice.catalog.domain.entity.Catalog;
import cleanhouse.catalogservice.catalog.domain.port.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CatalogRepositoryAdapter implements CatalogRepository {
    private final CatalogSpringDataRepository springDataRepository;
    @Qualifier("catalogModelMapper")
    private final ModelMapper modelMapper;

    @Override
    public Catalog save(Catalog catalog) {
        CatalogEntity entity = modelMapper.map(catalog, CatalogEntity.class);
        CatalogEntity savedEntity = springDataRepository.save(entity);
        return modelMapper.map(savedEntity, Catalog.class);
    }

    @Override
    public Optional<Catalog> findById(Long id) {
        return springDataRepository.findById(id)
                .map(entity -> modelMapper.map(entity, Catalog.class));
    }

    @Override
    public Optional<Catalog> findByProductId(String productId) {
        return springDataRepository.findByProductId(productId)
                .map(entity -> modelMapper.map(entity, Catalog.class));
    }

    @Override
    public List<Catalog> findAll() {
        return springDataRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, Catalog.class))
                .collect(Collectors.toList());
    }
}
